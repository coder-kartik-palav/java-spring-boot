# java-spring-boot
A modern full-stack web application that helps users track and eliminate debt using the Debt Snowball Method.

🚀 Features
🔐 Authentication
User Registration & Login (JWT based system)
📊 Debt Management
Add, delete, and manage multiple debts
Store data securely in MySQL database
🧮 Snowball Calculation
Automatically calculates:
Total months to payoff
Total interest paid
Month-by-month breakdown
📈 Interactive Dashboard
Real-time debt payoff chart (Recharts)
Beautiful glassmorphism UI
Dynamic theme switcher (Blue, Purple, Emerald, Rose)
🛠️ Tech Stack
Backend:
Java 17
Spring Boot 3
Spring Security (JWT)
Spring Data JPA
MySQL
Frontend:
React + Vite
Recharts
CSS (Glassmorphism UI)
📂 Project Structure
debt-snowball-app/
│
├── backend/
├── frontend/
└── README.md
⚙️ Prerequisites

Make sure you have installed:

Java 17+
Node.js 18+
MySQL (running on port 3306)
Maven (optional, wrapper included)
🗄️ Database Setup
Open MySQL
Create database:
CREATE DATABASE debt_snowball;
Update credentials in:
backend/src/main/resources/application.properties
spring.datasource.username=root
spring.datasource.password=root
▶️ Running the Backend (Spring Boot)
cd backend
mvnw.cmd spring-boot:run

Backend will start at:

http://localhost:8080
🎨 Running the Frontend (React)

Open new terminal:

cd frontend
npm install
npm run dev

Frontend will start at:

http://localhost:5173
🔄 Application Flow
Register a new user
Login (JWT authentication)
Add debts
System calculates payoff schedule
View results in chart form
🧪 API Example

POST request:

http://localhost:8080/api/calculator/snowball
{
  "extraMonthlyPayment": 200,
  "debts": [
    {
      "name": "Credit Card",
      "balance": 1500,
      "interestRate": 19.99,
      "minimumPayment": 50
    }
  ]
}
📌 Future Improvements
Deploy on cloud (AWS / Render)
Add notifications/reminders
Mobile responsive UI
Export reports (PDF/Excel)
👨‍💻 Author

Your Name
MCA Student | Full Stack Developer

⭐ If you like this project

Give it a ⭐ on GitHub!
