package com.example.vishalmegamartapp

object QuizRepository {
    fun getQuestions(): List<Question> {
        return listOf(
            Question(
                "CCTV mein ek banda chips khol ke kha raha hai. Aap kya karoge?",
                listOf("Chupke se ek chip maang lo", "Walkie-talkie se 'Code Crunch' bhejo", "Usse popcorn samajh ke enjoy karo", "Manager ko tag karo: ‘Live telecast chalu hai!’"),
                1
            ),
            Question(
                "Guard ki asli duty kya hai?",
                listOf("Sirf gate pe khade rehna", "Style mein entry aur killer smile dena", "Customers ko aankhon se scan karna", "Walkie-talkie leke movie hero banna"),
                2
            ),
            Question(
                "Kaunsa customer sabse zyada risky lagta hai?",
                listOf("Jo cute smile ke saath andar aata hai", "Jo kuch liye bina store ghoomta hai", "Jo 1 shampoo ke liye 2 ghante lagata hai", "Jo bas AC lene aata hai"),
                0
            ),
            Question(
                "Agar crush store mein aa jaye to kya karoge?",
                listOf("Bas aankhon se scan karunga", "Walkie-talkie se bolunga: ‘Dil gir gaya hai’", "Entry pe namaste + slow motion smile", "Professional rahunga, lekin heart alert chalu hoga"),
                1
            ),
            Question(
                "Guard ki walkie-talkie ka sabse cool use kya hai?",
                listOf("Security alert dena", "Chai mangwana", "Shayari sunana", "Boss banne ka feel lena"),
                3
            ),
            Question(
                "Sabse zyada chori hone wala item kya hai?",
                listOf("Toffee", "Socks", "Dil (guard ka)", "Perfume tester"),
                2
            ),
            Question(
                "Break time pe guard sabse pehle kya karta hai?",
                listOf("CCTV dekhte hue memes banata hai", "Walkie-talkie off karke chain ki saans leta hai", "Selfie kheenchta hai security pose mein", "Chai ke saath chips share karta hai"),
                0
            ),
            Question(
                "Customer bole: 'Bhaiya yeh kaha milega?' — aapka jawaab?",
                listOf("'Andar left mein, phir right'", "'Mujhe bhi nahi pata, par chalo chalte hain'", "'Wahi milega jahan likha hai'", "'Dil mein milega, product shelf pe!'"),
                1
            ),
            Question(
                "Kaunsa signal guard ke liye red alert hota hai?",
                listOf("Bachcha chocolate khol raha ho", "Ladka perfume try karke bhaag raha ho", "Aunty bina bag check kara entry le rahi ho", "Crush billing counter pe ho"),
                3
            ),
            Question(
                "Public aapko kis naam se yaad rakhegi?",
                listOf("Mr. Entry-Exit", "Charming Guard", "The Walkie-Talkie King", "Vishal ke Avengers se bhi zyada strong"),
                3
            )
        )
    }
}
