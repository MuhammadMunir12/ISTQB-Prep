package com.example.data

data class GlossaryTerm(
    val term: String,
    val definition: String,
    val analogy: String,
    val referenceSection: String
)

object GlossaryData {
    val terms = listOf(
        GlossaryTerm(
            term = "Verification",
            definition = "Confirmation by examination and provision of objective evidence that specified requirements have been fulfilled. Evaluates if the system matches the documented design. 'Are we building the product right?'",
            analogy = "Checking if a cake recipe calls for 3 eggs and confirming there are indeed 3 eggs in the mixing bowl.",
            referenceSection = "1.1"
        ),
        GlossaryTerm(
            term = "Validation",
            definition = "Confirmation by examination and provision of objective evidence that the requirements for a specific intended use or application have been fulfilled. Evaluates if the system satisfies user and stakeholder needs in its operational environment. 'Are we building the right product?'",
            analogy = "Tasting the baked cake to see if it actually tastes delicious and satisfies the customer's birthday party guests.",
            referenceSection = "1.1"
        ),
        GlossaryTerm(
            term = "Error (Mistake)",
            definition = "A human action that produces an incorrect result (such as writing incorrect code or documenting flawed requirements).",
            analogy = "A sleepy architect draws a door that opens directly into a brick wall.",
            referenceSection = "1.2"
        ),
        GlossaryTerm(
            term = "Defect (Bug / Fault)",
            definition = "An imperfection or deficiency in a work product where it does not meet its requirements or specifications. Created as a result of a human error.",
            analogy = "The physical blueprint containing the door drawn directly into the brick wall.",
            referenceSection = "1.2"
        ),
        GlossaryTerm(
            term = "Failure",
            definition = "An event in which a component or system does not perform a required function within specified limits. Occurs at runtime when a defect is executed.",
            analogy = "A person tries to walk through the door and slams their face directly into a solid brick wall.",
            referenceSection = "1.2"
        ),
        GlossaryTerm(
            term = "Debugging",
            definition = "The process of finding, analyzing, and removing the causes of failures in software. Done by developers, not testers.",
            analogy = "A construction foreman looking at the wall with a flashlight, identifying the bad blueprint drawing, and physically remodeling the door to open correctly.",
            referenceSection = "1.1"
        ),
        GlossaryTerm(
            term = "Regression Testing",
            definition = "Testing of a previously tested program following modification to ensure that defects have not been introduced or uncovered in unchanged areas of the software.",
            analogy = "After fixing a leaky faucet in the kitchen, turning on the bathroom shower to make sure the water pressure didn't drop or stop working.",
            referenceSection = "2.2"
        ),
        GlossaryTerm(
            term = "Confirmation Testing (Re-testing)",
            definition = "Testing that runs test cases that failed the last time they were run, in order to verify the success of corrective actions (bug fixes).",
            analogy = "Turning on the kitchen faucet again specifically to confirm that it is no longer leaking.",
            referenceSection = "2.2"
        ),
        GlossaryTerm(
            term = "Equivalence Partitioning (EP)",
            definition = "A black-box test technique in which test cases are designed to execute representatives from input or output partitions, assuming all elements in a partition behave identically.",
            analogy = "Assuming if one 10-year-old child is allowed on a roller coaster, all other children aged 6 to 12 in that same group will be handled identically.",
            referenceSection = "4.2"
        ),
        GlossaryTerm(
            term = "Boundary Value Analysis (BVA)",
            definition = "A black-box test technique in which test cases are designed based on boundary values of ordered equivalence partitions.",
            analogy = "Testing a movie theater's child ticket age limit of 12 by checking a child exactly aged 11, exactly aged 12, and exactly aged 13.",
            referenceSection = "4.2"
        ),
        GlossaryTerm(
            term = "Defect Masking",
            definition = "A situation in which one defect prevents the detection of another.",
            analogy = "A massive power outage (defect A) prevents you from discovering that the kitchen light bulb is burned out (defect B).",
            referenceSection = "4.2"
        ),
        GlossaryTerm(
            term = "Subsumes",
            definition = "A relationship between coverage criteria, where coverage A subsumes coverage B if any test suite that satisfies A also satisfies B (e.g. 100% Branch Coverage subsumes 100% Statement Coverage).",
            analogy = "Running 5 miles subsumes running 1 mile—if you completed the 5-mile run, you automatically covered the 1-mile distance.",
            referenceSection = "4.3"
        ),
        GlossaryTerm(
            term = "Test Charter",
            definition = "An outline of test objectives, and possibly test ideas, used in exploratory testing sessions.",
            analogy = "A map highlighting a forest region with instructions: 'Search the north riverbanks for golden mushrooms within 1 hour,' leaving the path to your discretion.",
            referenceSection = "4.4"
        ),
        GlossaryTerm(
            term = "Quality Assurance (QA)",
            definition = "Activities focused on providing confidence that quality requirements will be fulfilled. It is process-oriented, preventive, and applies to the entire development lifecycle.",
            analogy = "Establishing clean hygiene policies, baking training certifications, and ingredient checking procedures in a bakery.",
            referenceSection = "1.2"
        ),
        GlossaryTerm(
            term = "Quality Control (QC)",
            definition = "Activities focused on evaluating and identifying quality levels of final products. It is product-oriented and corrective (testing is a major part of QC).",
            analogy = "An inspector taking a cupcake from the tray, examining its size, checking the sugar content, and approving it for sale.",
            referenceSection = "1.2"
        )
    )

    fun searchTerms(query: String): List<GlossaryTerm> {
        if (query.isBlank()) return emptyList()
        val lowerQuery = query.lowercase()
        return terms.filter {
            it.term.lowercase().contains(lowerQuery) ||
            it.definition.lowercase().contains(lowerQuery)
        }
    }
}
