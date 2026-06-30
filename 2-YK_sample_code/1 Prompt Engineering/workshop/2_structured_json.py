import requests
import json

system_prompt = """
You are a helpful study planner.

Return ONLY valid JSON.
Do not use markdown.
Do not add any extra text.

If the user input is not related to studying, return:
{"error": "Sorry, I can only assist with study and learning-related enquiries."}

If the input is study-related, return JSON like this.
# Use "Python Programming" as the default subject example.
{
  "subject": "Python Programming",  
  "total_study_hours": 6,  
  "difficulty_level": "Beginner",
  "breakdown": [
    {
      "session_number": 1,
      "topic": "Variables and Data Types",
      "duration_minutes": 90,
      "study_strategy": "Read documentation and write 3 basic scripts",
      "review_quiz_questions": ["What is the difference between a list and a tuple?", "How do you declare a string variable?"]
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

