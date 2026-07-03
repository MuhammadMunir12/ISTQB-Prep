package com.example.data

data class SyllabusChapter(
    val id: Int,
    val title: String,
    val durationMinutes: Int,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val keywords: List<String>,
    val sections: List<SyllabusSection>
)

data class SyllabusSection(
    val id: String, // e.g. "1.1"
    val title: String,
    val learningObjectives: List<LearningObjective>,
    val content: SectionContent
)

data class LearningObjective(
    val code: String, // e.g. "FL-1.1.1"
    val level: String, // "K1", "K2", "K3"
    val description: String
)

data class SectionContent(
    val simpleExplanation: String,
    val detailedExplanation: String,
    val realWorldAnalogy: String,
    val softwareTestingExample: String,
    val interviewTips: String,
    val commonMistakes: String,
    val memoryTrick: String,
    val keyTakeaways: List<String>,
    val importantKeywords: List<String>
)

object SyllabusData {
    val chapters = listOf(
        SyllabusChapter(
            id = 1,
            title = "Fundamentals of Testing",
            durationMinutes = 180,
            difficulty = "Easy",
            keywords = listOf("debugging", "defect", "error", "failure", "quality", "quality assurance", "validation", "verification"),
            sections = listOf(
                SyllabusSection(
                    id = "1.1",
                    title = "What is Testing?",
                    learningObjectives = listOf(
                        LearningObjective("FL-1.1.1", "K1", "Identify typical test objectives"),
                        LearningObjective("FL-1.1.2", "K2", "Differentiate testing from debugging")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Testing is the process of evaluating a system to see if it does what it is supposed to do and to find issues before the users do.",
                        detailedExplanation = "Software testing is a set of activities to discover defects and evaluate the quality of software work products (known as test objects). It involves both verification (checking if we built the system correctly per specs) and validation (checking if the system meets stakeholder needs in its operational environment). Testing can be static (reviewing code, plans, or specs without running them) or dynamic (executing code to watch for failures).",
                        realWorldAnalogy = "Imagine proofreading a book: checking spelling is verification, but checking if readers actually enjoy the story is validation.",
                        softwareTestingExample = "Running a shopping cart app, adding a $10 item, and verifying that the checkout total shows exactly $10 plus tax, while checking if the payment gate triggers correctly.",
                        interviewTips = "Be ready to answer 'Is testing just finding bugs?'—No! It also includes evaluating work products, preventing defects, building confidence, complying with regulations, and verifying requirements.",
                        commonMistakes = "Thinking testing and debugging are the same. Testers find failures (testing); developers locate the root cause in code and fix it (debugging).",
                        memoryTrick = "V&V: Verification = 'Are we building the product RIGHT?' (Specs). Validation = 'Are we building the RIGHT product?' (User needs).",
                        keyTakeaways = listOf(
                            "Testing includes verification (spec match) and validation (user satisfaction).",
                            "Dynamic testing runs code; static testing reviews work products offline.",
                            "Debugging is a development activity, not a testing activity."
                        ),
                        importantKeywords = listOf("Verification", "Validation", "Static Testing", "Dynamic Testing", "Debugging")
                    )
                ),
                SyllabusSection(
                    id = "1.2",
                    title = "Why is Testing Necessary?",
                    learningObjectives = listOf(
                        LearningObjective("FL-1.2.1", "K2", "Exemplify why testing is necessary"),
                        LearningObjective("FL-1.2.2", "K1", "Recall the relation between testing and quality assurance"),
                        LearningObjective("FL-1.2.3", "K2", "Distinguish between root cause, error, defect, and failure")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Humans make mistakes (errors) that end up as bugs (defects) in code or specs. When that code runs, it leads to system malfunction (failures). Testing reduces risk of costly failures.",
                        detailedExplanation = "Quality Assurance (QA) is process-oriented and preventive—it ensures good processes are followed. Software testing is product-oriented, corrective, and falls under Quality Control (QC)—it uncovers product defects. Human error (e.g. distraction, tight deadline) leads to a defect (fault/bug) in code or requirements. When this defective item is executed, it manifests as a failure (deviation from correct behavior).",
                        realWorldAnalogy = "A tired baker (Error) forgets to add yeast to dough, creating flat dough (Defect). When customers eat it, they complain that it is hard as a rock (Failure).",
                        softwareTestingExample = "A developer under a tight timeline incorrectly uses '<' instead of '<=' (Error/Mistake) in code. This creates a bug (Defect) in the loop condition. During execution, a loop runs one less time than required, causing the screen to go blank (Failure).",
                        interviewTips = "Explain the progression: Human Error -> Code Defect -> Runtime Failure. Note that not all defects cause failures—some might never be executed.",
                        commonMistakes = "Using 'Error', 'Defect', and 'Failure' interchangeably. Remember, errors are committed by humans, defects are inside the work products, and failures happen in action.",
                        memoryTrick = "E-D-F-R: Error (by human) -> Defect (in product) -> Failure (at runtime). Root cause is the reason the error happened (e.g., fatigue).",
                        keyTakeaways = listOf(
                            "Testing reduces risk and contributes directly to product success.",
                            "QA improves processes to prevent bugs; testing is part of QC to detect them.",
                            "A defect in code doesn't cause a failure until that specific path is run."
                        ),
                        importantKeywords = listOf("Error", "Defect", "Failure", "Root Cause", "Quality Assurance", "Quality Control")
                    )
                ),
                SyllabusSection(
                    id = "1.3",
                    title = "Testing Principles",
                    learningObjectives = listOf(
                        LearningObjective("FL-1.3.1", "K2", "Explain the seven testing principles")
                    ),
                    content = SectionContent(
                        simpleExplanation = "There are 7 fundamental truths of software testing that guide all strategies, emphasizing that testing cannot be exhaustive or perfectly prove correctness.",
                        detailedExplanation = "The 7 Principles: \n1. Testing shows the presence, not the absence of defects (can't prove 100% bug-free).\n2. Exhaustive testing is impossible (can't test all input combinations).\n3. Early testing saves time and money (start static reviews early).\n4. Defects cluster together (Pareto principle: 80% bugs in 20% modules).\n5. Tests wear out (Pesticide paradox: repeat same tests, find no new bugs. Must vary them).\n6. Testing is context dependent (e-commerce is tested differently than a pacemaker).\n7. Absence-of-defects fallacy (building a bug-free system that no one wants is still a failure).",
                        realWorldAnalogy = "Pesticide Paradox: if you spray the same pesticide repeatedly, bugs build immunity. Likewise, unchanged test cases stop finding new defects.",
                        softwareTestingExample = "Varying test conditions to check price filters in an e-commerce app instead of executing the exact same login test 100 times.",
                        interviewTips = "Be ready to list all 7 principles. The interviewer's favorite is often 'Absence-of-defects fallacy'—finding all bugs is useless if the product fails to meet customer expectations (Validation is key!).",
                        commonMistakes = "Thinking that achieving 100% statement coverage means there are zero bugs. Principal 1: Testing can only show presence, never absolute absence of defects.",
                        memoryTrick = "S-E-E-C-W-C-A: Presence (S)hows bugs, (E)xhaustive impossible, (E)arly saves money, (C)lustering, (W)ear out, (C)ontext, (A)bsence fallacy.",
                        keyTakeaways = listOf(
                            "We can never prove a system is 100% perfect, but we can significantly reduce risk.",
                            "80% of defects are typically found in 20% of the code modules.",
                            "Vary test cases continuously to avoid tests wearing out."
                        ),
                        importantKeywords = listOf("Pesticide Paradox", "Defect Clustering", "Absence-of-defects Fallacy", "Exhaustive Testing")
                    )
                )
            )
        ),
        SyllabusChapter(
            id = 2,
            title = "Testing Throughout the SDLC",
            durationMinutes = 130,
            difficulty = "Medium",
            keywords = listOf("acceptance testing", "component testing", "regression testing", "shift left", "system testing", "test levels", "test types"),
            sections = listOf(
                SyllabusSection(
                    id = "2.1",
                    title = "Testing in the Context of an SDLC",
                    learningObjectives = listOf(
                        LearningObjective("FL-2.1.1", "K2", "Explain the impact of the chosen software development lifecycle on testing"),
                        LearningObjective("FL-2.1.2", "K1", "Recall good testing practices that apply to all software development lifecycles"),
                        LearningObjective("FL-2.1.3", "K1", "Recall the examples of test-first approaches to development"),
                        LearningObjective("FL-2.1.4", "K2", "Summarize how DevOps might have an impact on testing"),
                        LearningObjective("FL-2.1.5", "K2", "Explain shift left"),
                        LearningObjective("FL-2.1.6", "K2", "Explain how retrospectives can be used as a mechanism for process improvement")
                    ),
                    content = SectionContent(
                        simpleExplanation = "The way you develop software (Waterall vs Agile) changes when and how you test. Good testing dictates starting as early as possible (Shift-Left) and continuous improvement via team retrospectives.",
                        detailedExplanation = "Every development activity needs a corresponding test activity. Test-First approaches write tests before code (TDD: Test-Driven, ATDD: Acceptance Test-Driven, BDD: Behavior-Driven). DevOps builds synergy between Dev, Test, and Ops using automated CI/CD pipelines, offering fast feedback on code quality but risking neglecting manual exploratory verification. 'Shift Left' moves test activities as early as possible in the SDLC (e.g. reviewing requirements before coding, using static analysis, testing components early).",
                        realWorldAnalogy = "Building a house: Inspecting the blueprint (requirements review) is far cheaper than tearing down a finished brick wall (late defect fix) because the windows were in the wrong spot.",
                        softwareTestingExample = "A tester participates in a requirements workshop, questions the business rules for a checkout process, uncovers an ambiguity, and documents the acceptance criteria in Given/When/Then format (BDD) before developers write any code.",
                        interviewTips = "Under DevOps, testing must be fast, automated, and continuous. Explain that Shift-Left saves massive amounts of project budget by catching flaws in specifications.",
                        commonMistakes = "Thinking DevOps means 'automated testing only'. Fast pipelines are great, but human critical thinking and validation via manual testing are still vital.",
                        memoryTrick = "SHIFT-LEFT: Move testing to the LEFT of the timeline (start with requirements, not finished code).",
                        keyTakeaways = listOf(
                            "Every development activity should have a corresponding test activity.",
                            "TDD, BDD, and ATDD are test-first approaches where tests direct development.",
                            "DevOps relies on CI/CD pipelines to deliver fast code quality feedback."
                        ),
                        importantKeywords = listOf("TDD", "BDD", "ATDD", "Shift Left", "DevOps", "Retrospectives")
                    )
                ),
                SyllabusSection(
                    id = "2.2",
                    title = "Test Levels and Test Types",
                    learningObjectives = listOf(
                        LearningObjective("FL-2.2.1", "K2", "Distinguish the different test levels"),
                        LearningObjective("FL-2.2.2", "K2", "Distinguish the different test types"),
                        LearningObjective("FL-2.2.3", "K2", "Distinguish confirmation testing from regression testing")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Test Levels organize testing activities at different stages (Component, Integration, System, Acceptance). Test Types define what characteristics we are testing (Functional, Non-Functional, Black-Box, White-Box).",
                        detailedExplanation = "Test Levels: 1. Component (unit) testing (isolates code elements). 2. Component Integration testing (interfaces between modules). 3. System testing (overall behavior of entire system). 4. System Integration testing (interactions with external services). 5. Acceptance testing (validation for release readiness). Test Types: Functional (what it does), Non-Functional (how well it behaves—e.g. speed, security), Black-box (specification-based), and White-box (structure-based). Confirmation testing checks if a fixed bug is actually resolved. Regression testing checks if the fix accidentally broke other unchanged areas.",
                        realWorldAnalogy = "Automobile testing: testing a spark plug in isolation (Component), testing engine + transmission fit (Integration), driving the complete car on a track (System), and having a prospective buyer test drive it to see if they want to buy it (Acceptance).",
                        softwareTestingExample = "Checking if a memory leak occurs under heavy traffic (Non-Functional System Testing) vs checking if the user can log in with valid credentials (Functional System Testing).",
                        interviewTips = "Clarify the difference: Confirmation testing is running the *failed* test to confirm the fix; Regression testing is running *passed* tests to ensure nothing else got broken.",
                        commonMistakes = "Assuming component testing is done by testers. It is usually performed by developers in their local sandbox.",
                        memoryTrick = "C-I-S-A: Component -> Integration -> System -> Acceptance. (Alphabetical logic: C to A).",
                        keyTakeaways = listOf(
                            "Test levels organize testing milestones from isolated code to live systems.",
                            "Non-functional testing measures software behavior like performance, security, and portability.",
                            "Regression test suites grow over time and are prime candidates for automation."
                        ),
                        importantKeywords = listOf("Component Testing", "System Testing", "Acceptance Testing", "Confirmation Testing", "Regression Testing")
                    )
                )
            )
        ),
        SyllabusChapter(
            id = 3,
            title = "Static Testing",
            durationMinutes = 80,
            difficulty = "Medium",
            keywords = listOf("anomaly", "formal review", "informal review", "inspection", "review", "static analysis", "walkthrough"),
            sections = listOf(
                SyllabusSection(
                    id = "3.1",
                    title = "Static Testing Basics",
                    learningObjectives = listOf(
                        LearningObjective("FL-3.1.1", "K1", "Recognize types of work products that can be examined by static testing"),
                        LearningObjective("FL-3.1.2", "K2", "Explain the value of static testing"),
                        LearningObjective("FL-3.1.3", "K2", "Compare and contrast static testing and dynamic testing")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Static testing examines specs, requirements, or code without executing them, catching defects extremely early before compile or runtime.",
                        detailedExplanation = "Static testing can examine almost any work product (contracts, plans, specs, code, user stories). It finds defects directly (like a missing else-clause, typos, or dead code) rather than waiting for dynamic execution to cause a failure. It is incredibly cost-effective, improving communication and preventing defects downstream. Third-party binary code, which cannot be decompiled due to legal reasons, is not suitable for static review.",
                        realWorldAnalogy = "Spell-check in a word processor flag typos as you type (Static). Reading the printed document aloud to a group to hear how it sounds is closer to dynamic review.",
                        softwareTestingExample = "Using static analysis tools (like SonarQube or KtLint) to scan a Kotlin file to find unused declared variables or violations of formatting guidelines before committing code.",
                        interviewTips = "Static testing finds *defects* directly. Dynamic testing causes *failures* from which defects are found through debugging.",
                        commonMistakes = "Thinking static testing only means manual doc reviews. It also includes automated tool-based reviews (static analysis of code/architectures).",
                        memoryTrick = "Static = Stationary (no execution). Dynamic = Driving (code in motion).",
                        keyTakeaways = listOf(
                            "Almost any written work product can undergo static testing.",
                            "It finds defects directly, whereas dynamic testing reveals failures.",
                            "Early static testing drastically reduces debugging costs."
                        ),
                        importantKeywords = listOf("Static Analysis", "Code Quality", "Work Products", "Failures vs Defects")
                    )
                ),
                SyllabusSection(
                    id = "3.2",
                    title = "Feedback and Review Process",
                    learningObjectives = listOf(
                        LearningObjective("FL-3.2.1", "K1", "Identify the benefits of early and frequent stakeholder feedback"),
                        LearningObjective("FL-3.2.2", "K2", "Summarize the activities of the review process"),
                        LearningObjective("FL-3.2.3", "K1", "Recall which responsibilities are assigned to the principal roles when performing reviews"),
                        LearningObjective("FL-3.2.4", "K2", "Compare and contrast the different review types"),
                        LearningObjective("FL-3.2.5", "K1", "Recall the factors that contribute to a successful review")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Reviews follow a logical sequence (Planning, Initiation, Individual Review, Communication, Fixing). They range from Informal to extremely formal Inspections.",
                        detailedExplanation = "Review Process (ISO/IEC 20246): 1. Planning (define scope/exit criteria). 2. Review Initiation (distribute files). 3. Individual Review (detect anomalies). 4. Communication/Analysis (discuss findings). 5. Fixing/Reporting. Roles: Manager (decides review focus, provides resources), Author (creates/fixes work product), Moderator/Facilitator (leads meetings, mediates), Scribe/Recorder (logs anomalies), Reviewer (subject expert), Review Leader (takes overall responsibility). Review Types: Informal (no process, no logs), Walkthrough (author-led, educational), Technical Review (moderator-led, focus on technical decisions), Inspection (most formal, metric collection, author cannot be scribe or leader).",
                        realWorldAnalogy = "A formal courtroom trial (Inspection) has precise rules, a designated judge (Moderator), a transcriber (Scribe), and formal logs, whereas a casual debate among friends (Informal Review) has none of that.",
                        softwareTestingExample = "A formal Inspection of a banking security architecture document where the moderator leads, the security engineer acts as reviewer, a scribe logs 15 defects, and the document is rejected due to violating exit criteria.",
                        interviewTips = "Be clear on roles: Author cannot act as Scribe or Review Leader in a formal Inspection. This maintains objectivity.",
                        commonMistakes = "Thinking reviews are used to evaluate the performance of team members. Principle: Evaluation of participants must NEVER be an objective of a review.",
                        memoryTrick = "I-W-T-I: Informal -> Walkthrough -> Technical -> Inspection (Formality levels from lowest to highest).",
                        keyTakeaways = listOf(
                            "The generic review process comprises five distinct logical phases.",
                            "Inspectors log anomalies during individual review before any joint meetings.",
                            "Success depends on keeping reviews focused on small, manageable chunks of work."
                        ),
                        importantKeywords = listOf("Inspection", "Walkthrough", "Moderator", "Scribe", "Anomalies", "ISO/IEC 20246")
                    )
                )
            )
        ),
        SyllabusChapter(
            id = 4,
            title = "Test Analysis and Design",
            durationMinutes = 390,
            difficulty = "Hard",
            keywords = listOf("boundary value analysis", "branch coverage", "checklist-based testing", "decision table testing", "equivalence partitioning", "exploratory testing", "state transition testing", "statement coverage", "white-box test technique"),
            sections = listOf(
                SyllabusSection(
                    id = "4.1",
                    title = "Test Techniques Overview",
                    learningObjectives = listOf(
                        LearningObjective("FL-4.1.1", "K2", "Distinguish black-box, white-box and experience-based test techniques")
                    ),
                    content = SectionContent(
                        simpleExplanation = "We design test cases using three main families: Black-box (spec-based), White-box (code structure-based), and Experience-based (tester wisdom-based).",
                        detailedExplanation = "Black-box: derives tests from specs without checking code structure. White-box: derives tests from code structure, loops, and logic path coverage. Experience-based: relies on tester's intuition, knowledge, and history of typical developer errors.",
                        realWorldAnalogy = "Testing an ATM: entering a card and withdrawing money without knowing how it is wired (Black-Box), opening the ATM cabinet to trace the circuit boards and logic chips (White-Box), or kicking the card reader slot because you know older models jam there (Experience-Based).",
                        softwareTestingExample = "Designing inputs based on requirement REQ-10 (Black-Box) vs writing a test case to achieve 100% statement coverage in a Kotlin function (White-Box).",
                        interviewTips = "Remember that these techniques are complementary! Good strategies combine black-box specs with white-box coverage metrics and experience-based attacks.",
                        commonMistakes = "Saying white-box is only used by developers. Testers can use white-box techniques to measure code coverage achieved by their system tests.",
                        memoryTrick = "B-W-E: (B)lack-box = Outside specs, (W)hite-box = Inside code, (E)xperience = Tester brain.",
                        keyTakeaways = listOf(
                            "Black-box tests are independent of how the software is coded.",
                            "White-box tests require access to the source code structure.",
                            "Experience-based techniques are heavily dependent on the skills of the individual tester."
                        ),
                        importantKeywords = listOf("Black-Box", "White-Box", "Experience-Based", "Test Techniques")
                    )
                ),
                SyllabusSection(
                    id = "4.2",
                    title = "Black-Box Test Techniques",
                    learningObjectives = listOf(
                        LearningObjective("FL-4.2.1", "K3", "Use equivalence partitioning to derive test cases"),
                        LearningObjective("FL-4.2.2", "K3", "Use boundary value analysis to derive test cases"),
                        LearningObjective("FL-4.2.3", "K3", "Use decision table testing to derive test cases"),
                        LearningObjective("FL-4.2.4", "K3", "Use state transition testing to derive test cases")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Four key mathematical black-box techniques: Partitioning inputs into equal groups, testing boundaries of those groups, graphing complex business rules (Decision Tables), and mapping lifecycles (State Transitions).",
                        detailedExplanation = "Equivalence Partitioning (EP): divides inputs into valid/invalid partitions. Testing one value from a partition is sufficient. Boundary Value Analysis (BVA): exercises the edges of ordered partitions. 2-value BVA tests the boundary value and its closest neighbor. 3-value BVA tests the boundary and BOTH its immediate neighbors. Decision Table Testing: handles complex business logic combinations of conditions (T/F) and actions. State Transition Testing: tests how system transitions between states (e.g. Draft -> Pending -> Approved) based on events and guard conditions.",
                        realWorldAnalogy = "Vending machine: taking coins. Acceptable coins are $1, $2 (Valid Partition), washers or foreign coins are rejected (Invalid Partition). Testing a $1 coin covers the whole valid coin partition. BVA tests the age limits: for a restriction of age 18+, 2-value BVA tests 17 and 18; 3-value BVA tests 17, 18, and 19.",
                        softwareTestingExample = "A password length validator accepts 6 to 12 characters. Partitions: Too Short (<6), Valid (6-12), Too Long (>12). 2-value BVA values: 5, 6, 12, 13. 3-value BVA values: 5, 6, 7, 11, 12, 13.",
                        interviewTips = "Explain that 3-value BVA is more thorough than 2-value BVA. It is used for high-risk modules because it can detect off-by-one errors (like '<' instead of '<=') more reliably.",
                        commonMistakes = "Testing invalid equivalence partitions together. Important: Invalid partitions should be tested IN ISOLATION to avoid defect masking (one invalid input blocking the validation of another).",
                        memoryTrick = "E-B-D-S: (E)quivalence Partitions, (B)oundary Values, (D)ecision Tables, (S)tate Transitions.",
                        keyTakeaways = listOf(
                            "Invalid equivalence partitions must be tested one by one, not combined.",
                            "2-value BVA tests boundary + outer neighbor; 3-value tests inner neighbor + boundary + outer neighbor.",
                            "Decision tables map logic where conditions trigger specific action rules."
                        ),
                        importantKeywords = listOf("Equivalence Partitioning", "Boundary Value Analysis", "Decision Tables", "State Transition Testing", "Defect Masking")
                    )
                ),
                SyllabusSection(
                    id = "4.3",
                    title = "White-Box Test Techniques",
                    learningObjectives = listOf(
                        LearningObjective("FL-4.3.1", "K2", "Explain statement testing"),
                        LearningObjective("FL-4.3.2", "K2", "Explain branch testing"),
                        LearningObjective("FL-4.3.3", "K2", "Explain the value of white-box testing")
                    ),
                    content = SectionContent(
                        simpleExplanation = "White-box testing measures how much of the actual code instructions (Statements) or logical branch paths (Branches) are covered by our test cases.",
                        detailedExplanation = "Statement Testing/Coverage: aims to execute all executable code lines. Coverage % = (number of executed statements / total statements) * 100. Branch Testing/Coverage: aims to execute all decision branches (true/false paths). Coverage % = (number of executed branches / total branches) * 100. Note: Branch coverage SUBSUMES statement coverage (100% branch coverage guarantees 100% statement coverage, but NOT vice-versa).",
                        realWorldAnalogy = "Navigating a town: statement coverage is visiting every single street (node) at least once. Branch coverage is taking every turn at every intersection (edge) both left and right.",
                        softwareTestingExample = "Consider: `if (x > 10) print('high') else print('low')`. One test (x=15) executes the statement print('high') but misses the else branch. We need two tests (x=15, x=5) to achieve 100% branch coverage.",
                        interviewTips = "Always remember: 100% Statement Coverage does NOT mean 100% Branch Coverage. An 'if' without an 'else' can have 100% statement coverage with a single 'true' input, but misses the implicit 'false' branch path.",
                        commonMistakes = "Assuming white-box testing can find missing features. It CANNOT detect 'defects of omission' because it is based purely on code that already exists.",
                        memoryTrick = "Branch Subsumes Statement: B is stronger than S. A branch has two exits; a statement is just a line.",
                        keyTakeaways = listOf(
                            "Statement coverage only checks if code lines were visited.",
                            "Branch coverage tests all decision paths (true and false conditions).",
                            "White-box testing cannot find missing requirements."
                        ),
                        importantKeywords = listOf("Statement Coverage", "Branch Coverage", "Subsumes", "Defects of Omission")
                    )
                ),
                SyllabusSection(
                    id = "4.4",
                    title = "Experience-based Test Techniques",
                    learningObjectives = listOf(
                        LearningObjective("FL-4.4.1", "K2", "Explain error guessing"),
                        LearningObjective("FL-4.4.2", "K2", "Explain exploratory testing"),
                        LearningObjective("FL-4.4.3", "K2", "Explain checklist-based testing")
                    ),
                    content = SectionContent(
                        simpleExplanation = "In the absence of rigid specs, testers use experience: guessing common developer mistakes, testing with high-level checklists, or dynamically exploring (Exploratory Testing).",
                        detailedExplanation = "Error Guessing: anticipating bugs based on past developer mistakes or typical errors (e.g. division by zero, null pointers). Fault attack is a structured form of this. Exploratory Testing: simultaneous learning, test design, and execution. Uses a 'Test Charter' to timebox focus. Checklist-based: testing items from a checklist built on experience, ensuring flexibility and coverage.",
                        realWorldAnalogy = "A mechanic listening to a car engine: they don't read the manual step-by-step; they instantly check the spark plugs because they 'guess' that's where this model always fails.",
                        softwareTestingExample = "Creating a test charter: 'Explore the user registration screen with invalid dates to detect crash anomalies.' Session is timeboxed to 60 minutes and logged.",
                        interviewTips = "Exploratory testing is highly useful when specifications are missing or outdated, or when testing under severe time pressure.",
                        commonMistakes = "Thinking exploratory testing is just random clicking. It is structured, purposeful, and uses charters, logs, and timeboxes.",
                        memoryTrick = "E-E-C: (E)rror Guessing, (E)xploratory, (C)hecklists.",
                        keyTakeaways = listOf(
                            "Error guessing relies on intuitive anticipation of common developer faults.",
                            "Exploratory testing is highly structured and uses timeboxed charters.",
                            "Checklist-based tests allow variability, expanding coverage but reducing repeatability."
                        ),
                        importantKeywords = listOf("Error Guessing", "Exploratory Testing", "Test Charter", "Checklist-Based Testing", "Fault Attack")
                    )
                )
            )
        ),
        SyllabusChapter(
            id = 5,
            title = "Test Management",
            durationMinutes = 150,
            difficulty = "Hard",
            keywords = listOf("test plan", "test manager", "tester", "test estimation", "three-point estimation", "entry criteria", "exit criteria", "product risk", "project risk", "defect report"),
            sections = listOf(
                SyllabusSection(
                    id = "5.1",
                    title = "Test Planning and Estimation",
                    learningObjectives = listOf(
                        LearningObjective("FL-5.1.1", "K2", "Explain the purpose and content of a test plan"),
                        LearningObjective("FL-5.1.2", "K2", "Differentiate between a tester's and a test manager's tasks"),
                        LearningObjective("FL-5.1.3", "K2", "Summarize different test estimation techniques"),
                        LearningObjective("FL-5.1.4", "K3", "Apply three-point estimation")
                    ),
                    content = SectionContent(
                        simpleExplanation = "A test plan is a document that guides our testing. It defines what to test, how to test it, who does what, and when. Estimation helps predict the time, cost, and effort needed.",
                        detailedExplanation = "A test plan is a document describing the scope, approach, resources, and schedule of intended test activities. The role of the Test Manager includes planning, coordinating, monitoring, and selecting tools. The Tester's role is focused on designing, preparing, executing, and reporting tests. Test estimation techniques include: Metric-Based (using historic data or metrics) and Expert-Based (using expert experience, such as Wideband Delphi or Three-Point Estimation). Three-point estimation calculates the final estimate (E) as a weighted average of three scenarios: optimistic (a), most likely (m), and pessimistic (b) using the formula E = (a + 4m + b) / 6.",
                        realWorldAnalogy = "Planning a vacation: the organizer (Test Manager) books the hotels, budgets the trip, and sets the schedule. The travelers (Testers) actually explore the sights, report if a museum is closed, and log their experiences.",
                        softwareTestingExample = "A Test Manager writes a plan for a banking app's upgrade, specifying that security testing needs 2 engineers. Testers use 3-point estimation to calculate that executing 10 security tests will take 20 hours (Optimistic = 10h, Most Likely = 18h, Pessimistic = 38h; Formula: (10 + 4*18 + 38)/6 = 20 hours).",
                        interviewTips = "Be ready to explain the Three-Point Estimation formula: (a + 4m + b)/6. Also, emphasize that the Test Manager does NOT do all the testing; they facilitate and direct, while the tester performs the technical work.",
                        commonMistakes = "Thinking a test plan is static and never changes once written. In agile/DevOps, it is continuously updated as requirements change.",
                        memoryTrick = "TM vs Tester: TM is the director (Plan, Coordinate, Report), Tester is the actor (Design, Execute, Bug Logging).",
                        keyTakeaways = listOf(
                            "The test plan is a living document outlining scope, resources, and schedule.",
                            "Test Manager focuses on leadership and planning; Tester focuses on implementation and execution.",
                            "Three-point estimation uses a weighted average to handle uncertainty."
                        ),
                        importantKeywords = listOf("Test Plan", "Test Manager", "Tester", "Three-point estimation", "Metric-based estimation")
                    )
                ),
                SyllabusSection(
                    id = "5.2",
                    title = "Test Monitoring, Control and Completion",
                    learningObjectives = listOf(
                        LearningObjective("FL-5.2.1", "K2", "Explain how test progress is monitored and controlled"),
                        LearningObjective("FL-5.2.2", "K2", "Distinguish product risk from project risk"),
                        LearningObjective("FL-5.2.3", "K2", "Describe entry and exit criteria")
                    ),
                    content = SectionContent(
                        simpleExplanation = "We monitor test progress using metrics (like % of tests passed). If we fall behind, we take control actions (like adding resources). We use entry/exit criteria to know when to start and stop testing.",
                        detailedExplanation = "Test monitoring involves collecting metrics (e.g., test case execution status, defect density, resource utilization). Test control is taking corrective action based on those metrics (e.g., adjusting schedules, re-prioritizing tests). Entry criteria specify the conditions to start testing (e.g., test environment ready, code compiled). Exit criteria specify when to stop (e.g., 95% tests passed, all critical defects resolved). Product risk is the possibility that the software fails to meet user needs or has bugs (e.g., slow response, calculation error). Project risk is a threat to the project's success or timeline (e.g., staff shortage, scope creep, budget cuts).",
                        realWorldAnalogy = "Driving a car: monitoring is checking the speedometer (metrics). Control is stepping on the brakes if you are going too fast (corrective action). Entry criteria: engine turned on. Exit criteria: arriving safely at your destination.",
                        softwareTestingExample = "Monitoring reveals that only 50% of tests are executed with 2 days left (Metric). Test Control action: the manager schedules overtime or postpones low-priority test cases to focus on critical flows.",
                        interviewTips = "Differentiate Product Risk (quality of the app itself, e.g., security flaw) from Project Risk (issues with delivery, e.g., delay in API delivery). Interviewers love this.",
                        commonMistakes = "Confusing Entry/Exit criteria with Acceptance criteria. Entry/exit criteria are for testing activities, while acceptance criteria are for user story completion.",
                        memoryTrick = "Product risk = App fails. Project risk = Management/Delivery fails.",
                        keyTakeaways = listOf(
                            "Monitoring gathers data; control takes actions to keep tests on track.",
                            "Product risks relate to software quality; project risks relate to project constraints.",
                            "Exit criteria define when testing is complete, ensuring quality thresholds are met."
                        ),
                        importantKeywords = listOf("Test Monitoring", "Test Control", "Product Risk", "Project Risk", "Entry Criteria", "Exit Criteria")
                    )
                ),
                SyllabusSection(
                    id = "5.3",
                    title = "Configuration and Defect Management",
                    learningObjectives = listOf(
                        LearningObjective("FL-5.3.1", "K2", "Explain configuration management in testing"),
                        LearningObjective("FL-5.3.2", "K3", "Write a comprehensive defect report")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Configuration management keeps track of all our files (test cases, code, environments) so everything is in sync. Defect management is how we log, track, and resolve bugs.",
                        detailedExplanation = "Configuration Management (CM) ensures the integrity of test artifacts (test cases, test plans, test data, and test objects) by uniquely identifying, versioning, and tracking changes to them. Defect Management involves logging and tracking bugs. A standard defect report contains: unique ID, title, description, steps to reproduce, expected vs actual result, severity, priority, author, and state.",
                        realWorldAnalogy = "A library catalog: CM is the cataloging system that ensures every book version is tracked so you don't read an outdated edition. Defect management is reporting a torn page in a book so the librarian can fix it.",
                        softwareTestingExample = "Logging a defect for a payment failure: Steps to reproduce: 1. Add item to cart. 2. Select Credit Card. 3. Enter valid details. 4. Click Submit. Expected: Order confirmed. Actual: Error 500 displayed. Severity: High. Priority: High.",
                        interviewTips = "A great defect report must be objective, neutral, reproducible, and clear. Avoid emotional or blaming language like 'Developer forgot to code this'!",
                        commonMistakes = "Confusing Severity (technical impact, e.g. system crash) with Priority (business urgency, e.g., a typo in the homepage logo has low severity but high priority).",
                        memoryTrick = "CM = Control & Track Versions. Defect Report = Reproducible Steps.",
                        keyTakeaways = listOf(
                            "Configuration management ensures that all testing assets are versioned and traceable.",
                            "Defect reports must be objective and contain enough detail for developers to reproduce.",
                            "Severity is the technical impact; priority is the business urgency of a fix."
                        ),
                        importantKeywords = listOf("Configuration Management", "Defect Report", "Severity", "Priority", "Steps to Reproduce")
                    )
                )
            )
        ),
        SyllabusChapter(
            id = 6,
            title = "Test Tools",
            durationMinutes = 40,
            difficulty = "Easy",
            keywords = listOf("test tools", "static analysis tool", "test execution tool", "test automation", "pilot project"),
            sections = listOf(
                SyllabusSection(
                    id = "6.1",
                    title = "Tool Support for Testing",
                    learningObjectives = listOf(
                        LearningObjective("FL-6.1.1", "K1", "Recall different types of test tools"),
                        LearningObjective("FL-6.1.2", "K2", "Classify test tools according to their purpose")
                    ),
                    content = SectionContent(
                        simpleExplanation = "There are many types of tools that help us test: tools for managing tests, tools for static analysis, tools for executing tests, and tools for performance measurement.",
                        detailedExplanation = "Test tools can support direct testing (execution) or supporting activities. Classifications: 1. Test Management Tools (manage plans, requirements, defects, test execution status, e.g., Jira, TestRail). 2. Static Testing Tools (static analysis, linters, code quality tools). 3. Test Design Tools (generate test cases, test data, e.g., MBT). 4. Test Execution & Logging Tools (automate test execution, e.g., Selenium, Appium, JUnit). 5. Performance and Security Tools (simulate high load, check vulnerabilities, e.g., JMeter, OWASP ZAP).",
                        realWorldAnalogy = "Cooking a grand meal: you have tools to manage the recipe (Management), tools to inspect the ingredients (Static), tools to actually cook (Execution), and tools to measure temperatures (Performance).",
                        softwareTestingExample = "Using SonarQube (Static Analysis Tool) to inspect code complexity, followed by running JUnit (Test Execution Tool) to run unit tests, and checking execution in TestRail (Management Tool).",
                        interviewTips = "Know that static analysis tools can find defects before execution, saving time. Also, be ready to classify tools as supporting management, design, execution, or performance.",
                        commonMistakes = "Thinking a test tool is a magic bullet. Tools only execute what humans tell them to; a bad test case automated is just a bad test executed faster!",
                        memoryTrick = "M-S-D-E-P: Management, Static, Design, Execution, Performance.",
                        keyTakeaways = listOf(
                            "Tools range from supporting planning and management to direct automation of tests.",
                            "Static analysis tools run without executing code; execution tools run active code.",
                            "Select tools based on project constraints and team skill levels."
                        ),
                        importantKeywords = listOf("Test Tools", "Static Analysis Tool", "Test Execution Tool", "Test Management Tool")
                    )
                ),
                SyllabusSection(
                    id = "6.2",
                    title = "Benefits and Risks of Test Automation",
                    learningObjectives = listOf(
                        LearningObjective("FL-6.2.1", "K2", "Summarize benefits and risks of test automation"),
                        LearningObjective("FL-6.2.2", "K1", "Recall the principles for selecting and introducing tools")
                    ),
                    content = SectionContent(
                        simpleExplanation = "Automating tests makes them fast, repeatable, and reduces human error. However, it takes time, costs money to build and maintain, and can lead to unrealistic expectations.",
                        detailedExplanation = "Benefits of Automation: 1. Repeatability (same tests run identically). 2. Objective assessment (no human bias). 3. Speed of execution. 4. Ease of regression testing. Risks of Automation: 1. Unrealistic expectations (thinking automation solves all quality problems). 2. Overestimating time/cost savings (forgetting maintenance cost). 3. Neglecting manual testing. 4. Tool vendor going out of business. Selecting a tool should involve: pilot projects, evaluating vendor support, assessing team training needs, and calculating ROI.",
                        realWorldAnalogy = "Buying a robotic vacuum (Roomba): benefit is it sweeps everyday automatically (repeatability, effort reduction). Risk is you still have to clean its brushes (maintenance), it gets stuck on cords (limitations), and it cannot clean stairs (unrealistic expectations).",
                        softwareTestingExample = "A team automates 100 regression test cases. They run in 10 minutes instead of 2 days (Benefit). However, when the checkout screen changes, 50 automated tests break and require 20 hours of coding to fix (Risk: Maintenance Cost).",
                        interviewTips = "Discuss the 'pesticide paradox' in automation—if you run the exact same automated regression suite without ever updating it, it won't find new bugs. Manual exploratory testing remains essential.",
                        commonMistakes = "Assuming 100% of testing can or should be automated. Exploratory testing, usability testing, and UX validation require human cognition and can never be fully automated.",
                        memoryTrick = "Automation: High initial cost, huge speed benefits, high maintenance risks.",
                        keyTakeaways = listOf(
                            "Automation provides consistency, speed, and efficiency for regression tests.",
                            "Maintenance is the silent cost of test automation.",
                            "Always run a pilot project before adopting a new tool across the organization."
                        ),
                        importantKeywords = listOf("Test Automation", "Pilot Project", "Maintenance Cost", "Unrealistic Expectations")
                    )
                )
            )
        )
    )

    fun getChapterBySection(sectionId: String): SyllabusChapter? {
        val chapterId = sectionId.split(".").firstOrNull()?.toIntOrNull() ?: return null
        return chapters.find { it.id == chapterId }
    }
}
