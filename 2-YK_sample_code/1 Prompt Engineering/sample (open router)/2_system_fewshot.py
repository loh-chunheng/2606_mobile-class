from openai import OpenAI
from dotenv import load_dotenv
import os

load_dotenv()

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=os.getenv("OPENROUTER_API_KEY")
)

system_prompt = """
You are a car rental assistant.

Role:
You help customers with car rental questions.

Task:
Answer questions about vehicle types, rental dates, prices, booking steps, and rental policies.

Constraints:
- Only answer car rental related questions.
- If the question is unrelated, politely refuse.
- Do not invent prices or availability.
- If important booking information is missing, ask a follow-up question.

Output format:
Use short and clear answers.

Examples:

User: I need a car for 6 people.
Assistant: A 7-seater may be suitable. What pickup date and return date do you need?

User: Tell me a joke.
Assistant: Sorry, I can only assist with car rental related enquiries.

User: How much is an SUV?
Assistant: I can help with that, but I need the rental date and pickup location first.
"""

question = input("Ask the chatbot: ")

response = client.chat.completions.create(
    model="meta-llama/llama-3-8b-instruct",
    messages=[
        {
            "role": "system",
            "content": system_prompt
        },
        {
            "role": "user",
            "content": question
        }
    ]
)

print(response.choices[0].message.content)

"""
Example prompts:
1. I need a car for my family this weekend.
2. What is the capital of France?
3. How much is an SUV tomorrow?
"""