package com.example.data.api

import com.example.BuildConfig
import com.example.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

object AiTutorService {

    private val stopWords = setOf(
        "the", "a", "an", "and", "or", "but", "is", "are", "was", "were", "be", "been", "being",
        "to", "of", "in", "on", "at", "by", "for", "with", "about", "against", "between", "into",
        "through", "during", "before", "after", "above", "below", "from", "up", "down", "what",
        "how", "why", "where", "when", "who", "which", "this", "that", "these", "those"
    )

    /**
     * Client-Side RAG: Retrieves the most relevant syllabus sections and glossary terms
     * based on keyword overlap scoring.
     */
    fun retrieveRelevantContext(query: String): String {
        if (query.isBlank()) return ""

        val keywords = query.lowercase()
            .split(Regex("[^a-zA-Z0-9]+"))
            .filter { it.length > 2 && it !in stopWords }
            .toSet()

        if (keywords.isEmpty()) return ""

        // Score Glossary Terms
        val scoredGlossary = GlossaryData.terms.map { term ->
            var score = 0
            val termText = "${term.term} ${term.definition} ${term.analogy}".lowercase()
            for (kw in keywords) {
                if (term.term.lowercase() == kw) score += 15
                if (termText.contains(kw)) score += 2
            }
            term to score
        }.filter { it.second > 0 }
        .sortedByDescending { it.second }

        // Score Syllabus Sections
        val scoredSections = SyllabusData.chapters.flatMap { it.sections }.map { section ->
            var score = 0
            val sectionText = ("${section.title} ${section.content.simpleExplanation} " +
                    "${section.content.detailedExplanation} ${section.content.importantKeywords.joinToString(" ")}")
                    .lowercase()
            
            for (kw in keywords) {
                if (section.title.lowercase().contains(kw)) score += 10
                if (sectionText.contains(kw)) score += 2
                if (section.content.importantKeywords.any { it.lowercase() == kw }) score += 8
            }
            section to score
        }.filter { it.second > 0 }
        .sortedByDescending { it.second }

        val contextBuilder = StringBuilder()
        contextBuilder.append("=== OFFICIAL ISTQB KNOWLEDGE BASE CONTEXT ===\n\n")

        // Include top 2 glossary terms
        scoredGlossary.take(2).forEach { (term, _) ->
            contextBuilder.append("GLOSSARY TERM: ${term.term}\n")
            contextBuilder.append("DEFINITION: ${term.definition}\n")
            contextBuilder.append("ANALOGY: ${term.analogy}\n\n")
        }

        // Include top 2 syllabus sections
        scoredSections.take(2).forEach { (section, _) ->
            val chap = SyllabusData.getChapterBySection(section.id)
            contextBuilder.append("SYLLABUS SECTION ${section.id}: ${section.title} (Chapter: ${chap?.title})\n")
            contextBuilder.append("LEARNING OBJECTIVES: ${section.learningObjectives.joinToString(", ") { "${it.code} (${it.level}): ${it.description}" }}\n")
            contextBuilder.append("DETAILED SYLLABUS DETAIL:\n${section.content.detailedExplanation}\n")
            contextBuilder.append("REAL WORLD ANALOGY: ${section.content.realWorldAnalogy}\n")
            if (section.content.keyTakeaways.isNotEmpty()) {
                contextBuilder.append("KEY TAKEAWAYS:\n")
                section.content.keyTakeaways.forEach { takeaway -> contextBuilder.append("- $takeaway\n") }
            }
            contextStream(section.content, contextBuilder)
            contextBuilder.append("\n---\n")
        }

        return contextBuilder.toString()
    }

    private fun contextStream(content: SectionContent, sb: StringBuilder) {
        sb.append("COMMON TRAPS: ${content.commonMistakes}\n")
        sb.append("MEMO TRICK: ${content.memoryTrick}\n")
        sb.append("INTERVIEW TIP: ${content.interviewTips}\n")
    }

    /**
     * Builds a gorgeous, structured offline tutor response using retrieved RAG context when offline,
     * without API keys, or if the Gemini API is slow or times out.
     */
    fun buildOfflineSocratesResponse(query: String, retrievedContext: String): String {
        val sb = StringBuilder()
        sb.append("🦉 **Socrates Immediate Response (Syllabus Guide)**\n\n")
        sb.append("I have immediately retrieved the verified ISTQB CTFL 4.0 Syllabus reference for your query:\n\n")
        
        val lines = retrievedContext.split("\n")
        var hasContent = false
        var sectionTitle = ""
        
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("GLOSSARY TERM:")) {
                sb.append("📖 **Glossary Term:** ${trimmed.substringAfter("GLOSSARY TERM:").trim()}\n")
                hasContent = true
            } else if (trimmed.startsWith("DEFINITION:")) {
                sb.append("💡 **Definition:** ${trimmed.substringAfter("DEFINITION:").trim()}\n")
            } else if (trimmed.startsWith("ANALOGY:")) {
                sb.append("✨ **Analogy:** ${trimmed.substringAfter("ANALOGY:").trim()}\n\n")
            } else if (trimmed.startsWith("SYLLABUS SECTION")) {
                sectionTitle = trimmed.substringAfter("SYLLABUS SECTION").trim()
                sb.append("📚 **Syllabus Section $sectionTitle**\n")
                hasContent = true
            } else if (trimmed.startsWith("LEARNING OBJECTIVES:")) {
                sb.append("🎯 **Learning Objectives:** ${trimmed.substringAfter("LEARNING OBJECTIVES:").trim()}\n")
            } else if (trimmed.startsWith("DETAILED SYLLABUS DETAIL:")) {
                sb.append("📝 **Explanation:**\n${trimmed.substringAfter("DETAILED SYLLABUS DETAIL:").trim()}\n")
            } else if (trimmed.startsWith("REAL WORLD ANALOGY:")) {
                sb.append("🌍 **Real-World Analogy:** ${trimmed.substringAfter("REAL WORLD ANALOGY:").trim()}\n")
            } else if (trimmed.startsWith("KEY TAKEAWAYS:")) {
                sb.append("✅ **Key Takeaways:**\n")
            } else if (trimmed.startsWith("- ") && sectionTitle.isNotEmpty()) {
                sb.append("$trimmed\n")
            } else if (trimmed.startsWith("COMMON TRAPS:")) {
                sb.append("⚠️ **Common Traps:** ${trimmed.substringAfter("COMMON TRAPS:").trim()}\n")
            } else if (trimmed.startsWith("MEMO TRICK:")) {
                sb.append("🧠 **Socratic Memo Trick:** ${trimmed.substringAfter("MEMO TRICK:").trim()}\n")
            } else if (trimmed.startsWith("INTERVIEW TIP:")) {
                sb.append("💼 **Interview Tip:** ${trimmed.substringAfter("INTERVIEW TIP:").trim()}\n\n")
            }
        }
        
        if (!hasContent) {
            sb.append("I looked through the syllabus chapters, but couldn't find a direct keywords match for **\"$query\"**.\n\n")
            sb.append("Please try asking about core ISTQB concepts like **Boundary Value Analysis (BVA)**, **Equivalence Partitioning**, **Static Reviews**, **Test Levels**, **Testing Principles**, or **Pesticide Paradox**.")
        } else {
            sb.append("\n*Socrates has responded immediately using verified in-app syllabus context (Offline Safe).*")
        }
        
        return sb.toString()
    }

    /**
     * Submits a query along with chat history and retrieved RAG context to Gemini
     */
    suspend fun getTutorResponse(query: String, previousHistory: List<Part>): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        val retrievedContext = retrieveSyllabusContext(query)

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext buildOfflineSocratesResponse(query, retrievedContext)
        }
        
        val systemInstruction = "You are 'Socrates', an elite ISTQB Foundation Level AI Tutor. " +
                "Your objective is to help the user pass their CTFL 4.0 exam. " +
                "Be direct, highly concise, and clear. Avoid unnecessary wordiness. " +
                "You must base your explanations strictly on the provided OFFICIAL CONTEXT. " +
                "Do NOT invent, extrapolate, or hallucinate facts that are not present in the context. " +
                "If the query asks about details outside the context, politely explain that the syllabus doesn't explicitly state it. " +
                "Style: Friendly, academic, using simple software testing analogies. Provide helpful study mnemonics. " +
                "When reviewing questions, ALWAYS guide the student to the answer with hints first rather than giving it away directly."

        // Embed the last 6 turns of conversation history in the prompt to prevent API-side mismatch
        val historyStr = if (previousHistory.isNotEmpty()) {
            previousHistory.takeLast(6).joinToString("\n") { part ->
                part.text ?: ""
            }
        } else {
            ""
        }

        val prompt = "OFFICIAL SYLLABUS REFERENCE CONTEXT:\n$retrievedContext\n\n" +
                (if (historyStr.isNotEmpty()) "RECENT CONVERSATION HISTORY:\n$historyStr\n\n" else "") +
                "USER ENQUIRY: $query\n\n" +
                "Please answer the user concisely and directly based on the context. Keep your response under 3 paragraphs. If they are asking to explain a specific concept (e.g. BVA, boundary values, static reviews, pesticide paradox), reference the Learning Objective code and provide the real-world analogy and common mistakes."

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction))),
            generationConfig = GenerationConfig(
                temperature = 0.4f,
                maxOutputTokens = 550
            )
        )

        try {
            // Put a strict 4-second timeout on the network call to guarantee immediate response.
            kotlinx.coroutines.withTimeout(4000L) {
                val response = RetrofitClient.geminiService.generateContent(apiKey, request)
                response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                    ?: buildOfflineSocratesResponse(query, retrievedContext)
            }
        } catch (e: Exception) {
            buildOfflineSocratesResponse(query, retrievedContext)
        }
    }

    /**
     * Generates a brand new scenario-based question in the exact style of ISTQB 4.0
     */
    suspend fun generateAiQuestion(subject: String): ExamQuestion? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") return@withContext null

        val context = retrieveSyllabusContext(subject)

        val prompt = "Based on the following ISTQB Foundation Level syllabus context, generate ONE high-quality, " +
                "original multiple-choice scenario-based question in the style of the official CTFL exam. " +
                "Do not copy any of the official questions. Use believable realistic software test scenarios " +
                "(e.g., healthcare, e-commerce, banking, IoT, embedded systems).\n" +
                "Ensure distractors are plausible and look mathematically/logically correct but are wrong per syllabus criteria.\n\n" +
                "CONTEXT:\n$context\n\n" +
                "You MUST output the result strictly as a valid, parsable JSON object matching this structure:\n" +
                "{\n" +
                "  \"stem\": \"The question scenario text here...\",\n" +
                "  \"options\": [\"Option A\", \"Option B\", \"Option C\", \"Option D\"],\n" +
                "  \"correctIndex\": 0,\n" +
                "  \"rationale\": \"Detailed explanation of why the correct option is right and others are wrong...\",\n" +
                "  \"learningObjective\": \"FL-X.Y.Z\",\n" +
                "  \"kLevel\": \"K2 or K3\",\n" +
                "  \"difficulty\": \"Medium or Hard\",\n" +
                "  \"chapterId\": 1\n" +
                "}\n" +
                "Do not include any backticks or formatting. Just output raw, single-line or clean JSON."

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            generationConfig = GenerationConfig(
                temperature = 0.7f,
                responseMimeType = "application/json"
            )
        )

        try {
            val response = kotlinx.coroutines.withTimeout(4000L) {
                RetrofitClient.geminiService.generateContent(apiKey, request)
            }
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            if (jsonText != null) {
                val cleanedJson = jsonText.trim().removeSurrounding("```json", "```").trim()
                val jsonObject = JSONObject(cleanedJson)
                
                val optArray = jsonObject.getJSONArray("options")
                val options = List(optArray.length()) { optArray.getString(it) }

                return@withContext ExamQuestion(
                    id = "GENERATED_" + System.currentTimeMillis().toString(),
                    chapterId = jsonObject.optInt("chapterId", 1),
                    stem = jsonObject.getString("stem"),
                    options = options,
                    correctIndex = jsonObject.getInt("correctIndex"),
                    rationale = jsonObject.getString("rationale"),
                    learningObjective = jsonObject.getString("learningObjective"),
                    kLevel = jsonObject.getString("kLevel"),
                    difficulty = jsonObject.getString("difficulty"),
                    estimatedMinutes = if (jsonObject.getString("kLevel") == "K3") 2 else 1
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        null
    }

    private fun retrieveSyllabusContext(subject: String): String {
        val retrieved = retrieveRelevantContext(subject)
        return if (retrieved.length > 50) {
            retrieved
        } else {
            "Syllabus default overview: Covers chapters 1 to 6 of software testing fundamentals, black-box BVA & EP partitioning, white-box branch & statement coverage, static reviews, and risk-based testing."
        }
    }
}
