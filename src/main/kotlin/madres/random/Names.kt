package madres.random

/**
 * A test helper that generates names as needed.
 */
object TestNames {
    val firstNames = listOf(
        "Alex", "Jamie", "Taylor", "Jordan", "Morgan",
        "Casey", "Riley", "Drew", "Cameron", "Peyton",
        "Avery", "Bailey", "Charlie", "Dakota", "Elliot",
        "Finley", "Harper", "Jesse", "Kai", "Logan",
        "Micah", "Noel", "Oakley", "Quinn", "Reese",
        "Sage", "Skyler", "Tatum", "Zion", "Arden",
        "Blake", "Chris", "Devon", "Emery", "Frankie",
        "Gray", "Hunter", "Indigo", "Jay", "Kendall",
        "Lee", "Marlowe", "Nico", "Ollie", "Phoenix",
        "Remy", "Sasha", "Toby", "Val", "Winter",
        "Aiden", "Brielle", "Colby", "Delaney", "Ember",
        "Flynn", "Gianni", "Hayden", "Isla", "Jude",
        "Keegan", "Lennon", "Milan", "Nova", "Orion",
        "Presley", "River", "Sawyer", "Tegan", "Urban",
        "Vesper", "Wren", "Xander", "Yael", "Zuri",
        "Angel", "Blaine", "Cleo", "Dane", "Echo",
        "Fable", "Gale", "Hollis", "Ivory", "Jules",
        "Kit", "Lior", "Marley", "Nikita", "Onyx",
        "Pax", "Rain", "Scout", "Tory", "Uri",
        "Vale", "West", "Xen", "Yuri", "Zen"
    )

    val lastNames = listOf(
        "Smith", "Johnson", "Brown", "Taylor", "Anderson",
        "Thomas", "Jackson", "White", "Harris", "Martin",
        "Thompson", "Garcia", "Martinez", "Robinson", "Clark",
        "Rodriguez", "Lewis", "Lee", "Walker", "Hall",
        "Allen", "Young", "Hernandez", "King", "Wright",
        "Lopez", "Hill", "Scott", "Green", "Adams",
        "Baker", "Gonzalez", "Nelson", "Carter", "Mitchell",
        "Perez", "Roberts", "Turner", "Phillips", "Campbell",
        "Parker", "Evans", "Edwards", "Collins", "Stewart",
        "Sanchez", "Morris", "Rogers", "Reed", "Cook",
        "Morgan", "Bell", "Murphy", "Bailey", "Rivera",
        "Cooper", "Richardson", "Cox", "Howard", "Ward",
        "Torres", "Peterson", "Gray", "Ramirez", "James",
        "Watson", "Brooks", "Kelly", "Sanders", "Price",
        "Bennett", "Wood", "Barnes", "Ross", "Henderson",
        "Coleman", "Jenkins", "Perry", "Powell", "Long",
        "Patterson", "Hughes", "Flores", "Washington", "Butler",
        "Simmons", "Foster", "Gonzales", "Bryant", "Alexander",
        "Russell", "Griffin", "Diaz", "Hayes", "Myers",
        "Ford", "Hamilton", "Graham", "Sullivan", "Wallace"
    )

    fun randomFirstName() = firstNames.random()
    fun randomLastName() = lastNames.random()
    fun randomFullName() = "${randomFirstName()} ${randomLastName()}"
}