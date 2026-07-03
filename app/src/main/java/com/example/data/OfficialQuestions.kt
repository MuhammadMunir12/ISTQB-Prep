package com.example.data

data class ExamQuestion(
    val id: String, // e.g. "OFFICIAL_D_01"
    val chapterId: Int,
    val stem: String,
    val options: List<String>,
    val correctIndex: Int, // 0-based
    val multipleChoice: Boolean = false,
    val correctIndices: List<Int> = emptyList(), // for questions requiring multiple options
    val rationale: String,
    val learningObjective: String,
    val kLevel: String,
    val difficulty: String,
    val estimatedMinutes: Int = 1
)

object OfficialQuestions {
    val questions = listOf(
        // ==================== SAMPLE EXAM A ====================
        ExamQuestion(
            id = "OFFICIAL_A_01",
            chapterId = 1,
            stem = "Which of the following statements correctly describes a major test objective?",
            options = listOf(
                "Preventing defects through static work product reviews",
                "Confirming that all software engineers are thoroughly trained",
                "Ensuring 100% bug-free product delivery prior to release",
                "Drafting a complete business marketing roadmap"
            ),
            correctIndex = 0,
            rationale = "Preventing defects by participating in reviews of specifications, user stories, or code is a critical objective of testing (Option a is correct). Training developers is a management task (Option b is wrong). Proving a product is 100% bug-free is impossible per Principle 2 (Option c is wrong). Drafting marketing plans is not a testing objective (Option d is wrong).",
            learningObjective = "FL-1.1.1",
            kLevel = "K1",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_A_02",
            chapterId = 2,
            stem = "Which of the following is a key testing practice that applies to all Software Development Lifecycles (SDLCs)?",
            options = listOf(
                "Testing should start as early as possible in the lifecycle (Shift-Left)",
                "Independent testers must strictly perform system-level execution only",
                "Test automation is completely mandatory for all unit test cases",
                "Static analysis is only useful for safety-critical systems"
            ),
            correctIndex = 0,
            rationale = "Starting test activities as early as possible (Shift-Left) saves significant time and money and applies to all SDLCs (Option a is correct). Independent testing can exist at any level (Option b is wrong). Automation is helpful but not mandatory (Option c is wrong). Static analysis benefits all software products (Option d is wrong).",
            learningObjective = "FL-2.1.2",
            kLevel = "K1",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_A_03",
            chapterId = 3,
            stem = "What is the primary difference between a Walkthrough and an Inspection?",
            options = listOf(
                "A Walkthrough is led by the author, whereas an Inspection is led by a trained facilitator",
                "An Inspection has no formal process, whereas a Walkthrough has rigid metrics logging",
                "A Walkthrough focuses strictly on metrics, whereas an Inspection is purely educational",
                "Reviewers are optional in an Inspection, but they are required in a Walkthrough"
            ),
            correctIndex = 0,
            rationale = "A Walkthrough is led by the author of the work product, while an Inspection is highly formal and led by a trained Moderator or Facilitator who is not the author (Option a is correct). Inspections are extremely formal (Option b is wrong) and focus on metrics (Option c is wrong). Reviewers are required in both (Option d is wrong).",
            learningObjective = "FL-3.2.4",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_A_04",
            chapterId = 4,
            stem = "A tax calculator charges 5% tax on incomes up to $20,000, 10% on incomes between $20,001 and $50,000, and 15% on any income above $50,000. Using 3-value Boundary Value Analysis, what is the optimal set of values to test the lower boundary of the 10% tax bracket?",
            options = listOf(
                "$20,000, $20,001, $20,002",
                "$19,999, $20,000, $20,001",
                "$19,999, $20,001, $20,002",
                "$20,001, $50,000, $50,001"
            ),
            correctIndex = 1,
            rationale = "The lower boundary of the 10% bracket is $20,001. Using 3-value Boundary Value Analysis, we test the boundary itself ($20,001) and both of its immediate neighbors ($20,000 on the left, and $20,002 on the right). Thus, Option a ($20,000, $20,001, $20,002) is correct. Note that Option b represents 3-value BVA around $20,000.",
            learningObjective = "FL-4.2.2",
            kLevel = "K3",
            difficulty = "Hard"
        ),
        ExamQuestion(
            id = "OFFICIAL_A_05",
            chapterId = 5,
            stem = "Which of the following is considered a project risk rather than a product risk?",
            options = listOf(
                "Delay in the delivery of the test environment by a third-party vendor",
                "The system response time is slower than specified under load",
                "The payment gateway integration occasionally crashes during checkout",
                "The user documentation contains confusing terminology"
            ),
            correctIndex = 0,
            rationale = "Delay in the test environment delivery is a project risk because it threatens the project timeline and resources (Option a is correct). Slow response time, payment crashes, and confusing documentation are product risks related to the software quality itself (Options b, c, and d are wrong).",
            learningObjective = "FL-5.2.2",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_A_06",
            chapterId = 6,
            stem = "Which of the following is a major benefit of introducing a test execution tool?",
            options = listOf(
                "Reduction of repetitive manual work and much higher execution speed",
                "Automatic identification of missing or ambiguous business requirements",
                "Complete elimination of any future maintenance costs for test suites",
                "Absolute guarantee that there will be no false positive outcomes"
            ),
            correctIndex = 0,
            rationale = "Test execution tools automate repetitive tests, saving manual effort and running significantly faster (Option a is correct). Tools cannot find requirements of omission (Option b is wrong), still incur maintenance costs (Option c is wrong), and can experience false positives (Option d is wrong).",
            learningObjective = "FL-6.2.1",
            kLevel = "K2",
            difficulty = "Easy"
        ),

        // ==================== SAMPLE EXAM B ====================
        ExamQuestion(
            id = "OFFICIAL_B_01",
            chapterId = 1,
            stem = "How does software testing differ from debugging?",
            options = listOf(
                "Testing uncovers system failures, while debugging locates root causes and fixes defects",
                "Testing is entirely manual, while debugging is always fully automated",
                "Testing is performed by developers, while debugging is only done by business analysts",
                "Testing and debugging are exactly the same software quality activity"
            ),
            correctIndex = 0,
            rationale = "Testing executes software to watch for failures. Debugging is a development activity that isolates, analyzes, and fixes the root cause of those failures (Option a is correct).",
            learningObjective = "FL-1.1.2",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_B_02",
            chapterId = 2,
            stem = "What is the primary goal of Acceptance Testing?",
            options = listOf(
                "To build confidence that the system is ready for release and meets user needs",
                "To find as many deep performance memory leaks as possible",
                "To achieve 100% statement and branch code coverage",
                "To check the internal software class and method designs"
            ),
            correctIndex = 0,
            rationale = "Acceptance testing focuses on validating that the system matches stakeholder needs, establishing confidence in release readiness (Option a is correct).",
            learningObjective = "FL-2.2.1",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_B_03",
            chapterId = 3,
            stem = "Which review role is responsible for documenting all anomalies, defects, and feedback found during a review meeting?",
            options = listOf(
                "Scribe (Recorder)",
                "Moderator (Facilitator)",
                "Author",
                "Review Leader"
            ),
            correctIndex = 0,
            rationale = "The Scribe (or Recorder) is responsible for logging all anomalies and details discussed during the review session (Option a is correct).",
            learningObjective = "FL-3.2.3",
            kLevel = "K1",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_B_04",
            chapterId = 4,
            stem = "You are testing an input field that accepts a numeric percentage between 1 and 100 inclusive. Using 2-value Boundary Value Analysis, which set of inputs is correct to verify the boundaries?",
            options = listOf(
                "0, 1, 100, 101",
                "1, 2, 99, 100",
                "0, 2, 99, 101",
                "1, 50, 100"
            ),
            correctIndex = 0,
            rationale = "The boundaries are 1 and 100. In 2-value BVA, we test the boundary value and its closest outer neighbor. For 1, neighbors are 0 (outer) and 2 (inner); so we select 0 and 1. For 100, neighbors are 99 (inner) and 101 (outer); so we select 100 and 101 (Option a is correct).",
            learningObjective = "FL-4.2.2",
            kLevel = "K3",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_B_05",
            chapterId = 5,
            stem = "Using the three-point estimation formula, calculate the final test effort if Optimistic = 5 hours, Most Likely = 8 hours, and Pessimistic = 17 hours.",
            options = listOf(
                "9 person-hours",
                "10 person-hours",
                "8 person-hours",
                "11 person-hours"
            ),
            correctIndex = 0,
            rationale = "Three-point estimation is calculated as: E = (a + 4m + b) / 6. Here: a = 5, m = 8, b = 17. E = (5 + 4*8 + 17) / 6 = (5 + 32 + 17)/6 = 54 / 6 = 9 hours (Option a is correct).",
            learningObjective = "FL-5.1.4",
            kLevel = "K3",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_B_06",
            chapterId = 6,
            stem = "Which of the following is a tool classification that supports static testing?",
            options = listOf(
                "Linters and static code analysis tools",
                "Test execution tools like Selenium or Playwright",
                "Defect tracking databases like Jira",
                "Performance load tools like Apache JMeter"
            ),
            correctIndex = 0,
            rationale = "Static analysis tools and linters inspect code structure and rules without executing the code, supporting static testing directly (Option a is correct).",
            learningObjective = "FL-6.1.2",
            kLevel = "K2",
            difficulty = "Easy"
        ),

        // ==================== SAMPLE EXAM C ====================
        ExamQuestion(
            id = "OFFICIAL_C_01",
            chapterId = 1,
            stem = "Which testing principle emphasizes that finding and fixing bugs is useless if the system does not meet user needs?",
            options = listOf(
                "Absence-of-defects fallacy",
                "Pesticide paradox",
                "Exhaustive testing is impossible",
                "Early testing saves time"
            ),
            correctIndex = 0,
            rationale = "The 'absence-of-defects fallacy' principle highlights that resolving all errors is meaningless if the product built does not satisfy stakeholder needs or requirements (Option a is correct).",
            learningObjective = "FL-1.3.1",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_02",
            chapterId = 2,
            stem = "In a DevOps model, which practice aims to provide continuous feedback on code quality as early as possible?",
            options = listOf(
                "Continuous Integration / Continuous Deployment (CI/CD)",
                "Sequential phased waterfall validation",
                "User acceptance walkthroughs",
                "Post-release incident logging"
            ),
            correctIndex = 0,
            rationale = "DevOps leverages automated CI/CD pipelines to build, deploy, and test code continuously, ensuring developers receive instant quality feedback (Option a is correct).",
            learningObjective = "FL-2.1.4",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_03",
            chapterId = 3,
            stem = "During which phase of the formal review process are the review artifacts distributed and instructions given to review participants?",
            options = listOf(
                "Review Initiation",
                "Planning",
                "Individual Review",
                "Communication and Analysis"
            ),
            correctIndex = 0,
            rationale = "In the Review Initiation phase, the Moderator/Facilitator distributes the work products and briefs reviewers on objectives and tasks (Option a is correct).",
            learningObjective = "FL-3.2.2",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_04",
            chapterId = 4,
            stem = "What is the key difference between Statement Coverage and Branch Coverage?",
            options = listOf(
                "Branch coverage tests decision paths (true and false conditions) and subsumes statement coverage",
                "Statement coverage is a structural white-box technique, while branch coverage is black-box",
                "Achieving 100% statement coverage mathematically guarantees 100% branch coverage",
                "Statement coverage tests all possible execution speeds while branch coverage tests API integrations"
            ),
            correctIndex = 0,
            rationale = "Branch coverage checks both the true and false outcomes of decisions. Achieving 100% branch coverage guarantees 100% statement coverage (subsumes it), but not vice-versa (Option a is correct).",
            learningObjective = "FL-4.3.1",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_05",
            chapterId = 5,
            stem = "Which criteria determines whether testing activities can actually commence, such as verifying the test environment is fully prepared?",
            options = listOf(
                "Entry Criteria",
                "Exit Criteria",
                "Acceptance Criteria",
                "Definition of Done"
            ),
            correctIndex = 0,
            rationale = "Entry criteria define the conditions that must be satisfied before starting a specific testing activity (Option a is correct). Exit criteria specify when to stop (Option b is wrong).",
            learningObjective = "FL-5.2.3",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_06",
            chapterId = 6,
            stem = "Why is a pilot project highly recommended when introducing a new test tool to an organization?",
            options = listOf(
                "To assess tool compatibility, train the team, and establish standards",
                "To completely eliminate the need for tool licensing budgets",
                "To guarantee that no automated tests will ever break",
                "To bypass the need for any management review"
            ),
            correctIndex = 0,
            rationale = "A pilot project is used to learn the tool's detailed capabilities, assess its fit with existing workflows, train a core team, and define standards for usage (Option a is correct).",
            learningObjective = "FL-6.2.2",
            kLevel = "K2",
            difficulty = "Medium"
        ),

        // ==================== SAMPLE EXAM D ====================
        ExamQuestion(
            id = "OFFICIAL_D_01",
            chapterId = 1,
            stem = "Which of the following is a typical test objective?",
            options = listOf(
                "Finding and fixing defects in the test object",
                "Maintaining effective communications with developers",
                "Validating that legal requirements have been met",
                "Building confidence in the quality of the test object"
            ),
            correctIndex = 3,
            rationale = "Building confidence in the quality of the test object is achieved by executing tests that passed (Option d). Fixing defects is not a test activity (Option a is wrong). Maintaining communication is a supporting activity, not a primary objective (Option b is wrong). Checking legal requirements is a form of verification, whereas validation concerns stakeholders' operational needs (Option c is wrong).",
            learningObjective = "FL-1.1.1",
            kLevel = "K1",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_D_02",
            chapterId = 1,
            stem = "A designer documents a design for a user interface that does not suitably address disabled users because the designer is tired. The programmer implements the user interface in line with the design but as they are working under severe time pressure, they do not include suitable exception handling in their program code for bonus calculations. When the operational system is used, complaints are made by some disabled users about the interface and the company is subsequently fined by the regulatory authority. No one notices that bonus calculations are sometimes incorrect.\n\nWhich of the following statements is CORRECT?",
            options = listOf(
                "The miscalculation of bonuses is a defect that occasionally occurs",
                "The fine received for failing to address some disabled users is a failure",
                "The programmer working under severe time pressure is a root cause",
                "The design of the user interface includes a designer error"
            ),
            correctIndex = 2,
            rationale = "The programmer working under severe time pressure led directly to the mistake (error), making it the root cause of the subsequent defect in the code (Option c is correct). The miscalculation of bonuses is a failure, not a defect (Option a is wrong). The fine itself is a penalty, whereas the inaccessible interface is the failure (Option b is wrong). The UI design includes a design defect, not a designer error (Option d is wrong).",
            learningObjective = "FL-1.2.3",
            kLevel = "K2",
            difficulty = "Medium",
            estimatedMinutes = 2
        ),
        ExamQuestion(
            id = "OFFICIAL_D_03",
            chapterId = 1,
            stem = "Test conditions are being used by testers to generate test cases and execute tests. Even though the test conditions remain the same, the test cases are varied each time. Which of the following 'principles of testing' is being addressed through the variation of test cases?",
            options = listOf(
                "Tests wear out",
                "Absence-of-defects fallacy",
                "Early testing saves time and money",
                "Defects cluster together"
            ),
            correctIndex = 0,
            rationale = "The 'tests wear out' principle (also historically known as the pesticide paradox) states that repeating identical tests on unchanged code is unlikely to find new bugs; therefore, varying test cases is essential (Option a is correct).",
            learningObjective = "FL-1.3.1",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_D_21",
            chapterId = 4,
            stem = "You are testing a temperature control system for a horticultural cold storage facility. The system receives the temperature (in full degrees Celsius) as the input. If the temperature is between 0 and 2 degrees inclusive, the system displays the message 'temperature OK'. For lower temperatures, the system displays 'temperature too low' and for higher temperatures it displays 'temperature too high'.\n\nUsing two-value boundary value analysis, which of the following sets of test inputs provides the highest level of boundary value coverage?",
            options = listOf(
                "–1, 3",
                "0, 2",
                "–1, 0, 2, 3",
                "–2, 0, 2, 4"
            ),
            correctIndex = 2,
            rationale = "The equivalence partitions are: {..., -2, -1}, {0, 1, 2}, {3, 4, ...}. For 2-value BVA, the boundary values are the partition limits (-1, 0, 2, 3). Thus, testing exactly –1, 0, 2, 3 covers all boundary values and neighbors (Option c is correct).",
            learningObjective = "FL-4.2.2",
            kLevel = "K3",
            difficulty = "Hard",
            estimatedMinutes = 2
        ),
        ExamQuestion(
            id = "OFFICIAL_C_21",
            chapterId = 4,
            stem = "A developer was asked to implement the following business rule:\nINPUT: value (integer number)\nIF (value <= 100 OR value >= 200) THEN write 'value incorrect' ELSE write 'value OK'\n\nYou design the test cases using 2-value boundary value analysis. Which of the following sets of test inputs achieves the greatest boundary coverage?",
            options = listOf(
                "100, 150, 200, 210",
                "99, 100, 200, 201",
                "98, 99, 100, 101",
                "101, 150, 199, 200"
            ),
            correctIndex = 1,
            rationale = "The partitions are: {..., 100} (value incorrect), {101, ..., 199} (value OK), {200, ...} (value incorrect). The boundary values are 100, 101, 199, and 200. Option b provides [99, 100, 200, 201] which covers the boundaries 100 and 200 and their outer neighbors, offering the highest boundary coverage of 2-value BVA (Option b is correct).",
            learningObjective = "FL-4.2.2",
            kLevel = "K3",
            difficulty = "Hard",
            estimatedMinutes = 2
        ),
        ExamQuestion(
            id = "OFFICIAL_B_20",
            chapterId = 4,
            stem = "Customers of a car wash chain have cards recording the number of washes bought. The initial value is 0. Each wash increases the count by one. For every tenth wash (e.g. 10th, 20th...) the system gives a 10% discount, and for every twentieth wash, the system gives a further 40% discount (50% in total).\n\nWhich of the following sets of input data (current wash counts) achieves the highest valid equivalence partition coverage?",
            options = listOf(
                "19, 20, 30",
                "11, 12, 20",
                "1, 10, 50",
                "10, 29, 30, 31"
            ),
            correctIndex = 0,
            rationale = "The three valid equivalence partitions are: No discount (e.g., 19), 10% discount (every tenth wash like 30), and 50% discount (every twentieth wash like 20). Option a contains 19 (no discount), 20 (50% discount), and 30 (10% discount), which covers all three partitions perfectly (Option a is correct).",
            learningObjective = "FL-4.2.1",
            kLevel = "K3",
            difficulty = "Medium",
            estimatedMinutes = 2
        ),
        ExamQuestion(
            id = "OFFICIAL_A_32",
            chapterId = 5,
            stem = "Your team uses the three-point estimation technique to estimate the test effort for a new high-risk feature. The following estimates were made:\n- Most optimistic estimation: 2 person-hours\n- Most likely estimation: 11 person-hours\n- Most pessimistic estimation: 14 person-hours\n\nWhat is the final estimate?",
            options = listOf(
                "9 person-hours",
                "14 person-hours",
                "11 person-hours",
                "10 person-hours"
            ),
            correctIndex = 3,
            rationale = "Three-point estimation uses the weighted arithmetic mean formula: E = (a + 4m + b) / 6. In this case, a = 2, m = 11, b = 14. E = (2 + 4 * 11 + 14) / 6 = (2 + 44 + 14) / 6 = 60 / 6 = 10 person-hours (Option d is correct).",
            learningObjective = "FL-5.1.4",
            kLevel = "K3",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_D_31",
            chapterId = 5,
            stem = "The team wants to estimate the time needed for one tester to execute four test cases for a software component. The team has gathered the following measures of effort to execute a single test case:\n- Best-case scenario: 1 hour\n- Worst-case scenario: 8 hours\n- Most likely scenario: 3 hours\n\nGiven that the three-point estimation technique is being used, what is the final estimate of the time needed to execute all four test cases?",
            options = listOf(
                "14 hours",
                "3.5 hours",
                "16 hours",
                "12 hours"
            ),
            correctIndex = 0,
            rationale = "First, find the estimate for a single test case: E = (1 + 4 * 3 + 8) / 6 = 21 / 6 = 3.5 hours. For executing 4 test cases: 3.5 hours * 4 = 14 hours (Option a is correct).",
            learningObjective = "FL-5.1.4",
            kLevel = "K3",
            difficulty = "Medium",
            estimatedMinutes = 2
        ),
        ExamQuestion(
            id = "OFFICIAL_D_15",
            chapterId = 3,
            stem = "Which of the following CANNOT be examined by static testing?",
            options = listOf(
                "Contract",
                "Test plan",
                "Encrypted code",
                "Test charter"
            ),
            correctIndex = 2,
            rationale = "Encrypted code cannot be examined because it is too complex for human interpretation and cannot be processed by tools (Option c is correct). Contracts, plans, and charters can be reviewed manually (Options a, b, and d are wrong).",
            learningObjective = "FL-3.1.1",
            kLevel = "K1",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_D_16",
            chapterId = 3,
            stem = "Which of the following statements about the value of static testing is CORRECT?",
            options = listOf(
                "The defect types found by static testing are different from the defect types that can be found by dynamic testing",
                "Dynamic testing can detect the defect types that can be found by static testing plus some additional defect types",
                "Dynamic testing can identify some of the defects that can be found by static testing but not all of them",
                "Static testing can identify the defect types that can be found by dynamic testing as well as some extra defect types"
            ),
            correctIndex = 2,
            rationale = "Dynamic testing can identify some defects that static testing can find, but definitely not all of them (such as defects in non-executable work products like specifications, or dead code that can never run). Therefore, Option c is correct.",
            learningObjective = "FL-3.1.2",
            kLevel = "K2",
            difficulty = "Medium"
        ),
        ExamQuestion(
            id = "OFFICIAL_D_35",
            chapterId = 5,
            stem = "Which of the following are product risks?",
            options = listOf(
                "Scope creep and cost-cutting",
                "Poor architecture and too long response time",
                "Cost-cutting and poor tool support",
                "Poor tool support and scope creep"
            ),
            correctIndex = 1,
            rationale = "Product risks relate to the quality of the software itself (poor architecture, long response time). Project risks relate to the budget, schedule, and team resources (cost-cutting, scope creep, poor tool support). Hence, Option b is correct.",
            learningObjective = "FL-5.2.2",
            kLevel = "K2",
            difficulty = "Easy"
        ),
        ExamQuestion(
            id = "OFFICIAL_C_13",
            chapterId = 2,
            stem = "Which of the following test levels is MOST likely being performed if the testing is focused on validation and is not being performed by professional testers?",
            options = listOf(
                "Component testing",
                "Component integration testing",
                "System integration testing",
                "Acceptance testing"
            ),
            correctIndex = 3,
            rationale = "Acceptance testing focuses on validating that the system meets user needs and is ready for deployment. It is typically performed by business users, clients, or product owners rather than professional testers (Option d is correct).",
            learningObjective = "FL-2.2.1",
            kLevel = "K2",
            difficulty = "Easy"
        )
    )

    fun getQuestionsForChapter(chapterId: Int): List<ExamQuestion> {
        return questions.filter { it.chapterId == chapterId }
    }

    fun getQuestionsForSampleExam(examType: String): List<ExamQuestion> {
        val prefix = "OFFICIAL_${examType.uppercase()}"
        return questions.filter { it.id.startsWith(prefix) }
    }
}
