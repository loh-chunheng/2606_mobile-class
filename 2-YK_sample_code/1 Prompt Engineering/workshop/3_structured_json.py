import requests
import json

system_prompt = """
You are a helpful study planner.

Return ONLY valid JSON.
Do not use markdown.
Do not add any extra text.

If the user input is not related to studying, learning, test prep, or academic planning, return:
{"error": "Sorry, I can only assist with study and learning-related enquiries."}

If the input is study-related, return JSON that matches this structure:
{
  "subject": "Python Programming",
  "total_study_hours": 6,
  "difficulty_level": "Beginner",
  "breakdown": [
    {
      "session_number": 1,
      "topic": "Variables, Data Types, and Basic Operators",
      "duration_minutes": 90,
      "study_strategy": "Read documentation on strings, integers, and floats. Write 3 scripts practicing type casting.",
      "review_quiz_questions": [
        "What is the key difference between a list and a tuple in Python?",
        "How do you convert the string '123' into an integer?"
      ]
    },
    {
      "session_number": 2,
      "topic": "Control Flow (If-Else Statements and Loops)",
      "duration_minutes": 90,
      "study_strategy": "Watch a 20-minute tutorial on for/while loops. Code a simple guessing game using a while loop.",
      "review_quiz_questions": [
        "What is the purpose of the 'elif' statement?",
        "How do you break out of an infinite loop prematurely?"
      ]
    },
    {
      "session_number": 3,
      "topic": "Functions and Scope",
      "duration_minutes": 90,
      "study_strategy": "Practice defining functions with def. Write functions that accept arguments and return values.",
      "review_quiz_questions": [
        "What happens if a function does not have a 'return' statement?",
        "What is the difference between a global variable and a local variable?"
      ]
    },
    {
      "session_number": 4,
      "topic": "Comprehensive Review and Mini-Project",
      "duration_minutes": 90,
      "study_strategy": "Build a command-line calculator that uses functions, loops, and conditional logic. Review quiz mistakes.",
      "review_quiz_questions": [
        "How would you refactor repetitive code into a reusable function?",
        "Describe how you would debug a SyntaxError in your script."
      ]
    }
  ]
}
"""

question = input("User: ")
prompt = system_prompt + "\n\nUser message:\n" + question

data = {
    "model": "llama3",
    "prompt": prompt,
    "stream": False
}

response = requests.post("http://localhost:11434/api/generate", json=data)
answer = response.json()["response"]

print("\nRaw model output:")
print(answer)

try:
    returned_data = json.loads(answer)
    print("\nParsed Python dictionary:")
    print(returned_data)
except json.JSONDecodeError:
    print("\nError: Model did not return valid JSON.")


