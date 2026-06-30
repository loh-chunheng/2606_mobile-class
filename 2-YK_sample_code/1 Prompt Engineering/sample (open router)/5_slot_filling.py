# Input: Collects arrival, departure, budget
# Generate Itinerary
# No validation, no failure handling, reliability is low

from openai import OpenAI
import os

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=os.getenv("OPENROUTER_API_KEY")
)

# MODEL = "meta-llama/llama-3-8b-instruct"
MODEL = "google/gemma-4-26b-a4b-it:free"

memory = {
    "arrival": None,
    "departure": None,
    "budget": None
}

questions = [
    ("arrival", "What is your arrival date?"),
    ("departure", "What is your departure date?"),
    ("budget", "What is your total budget?")
]

print("Singapore Travel Assistant")
print("Type 'exit' anytime to quit.\n")

# Collect answers one by one
for key, question in questions:
    user = input(question + " ")

    if user.lower() == "exit":
        print("Program ended.")
        exit()

    memory[key] = user

print("\nDEBUG MEMORY:", memory)

# Generate itinerary
final_prompt = f"""
You are now a Singapore itinerary generator.

Trip details:
- Arrival date: {memory["arrival"]}
- Departure date: {memory["departure"]}
- Budget: {memory["budget"]}

Create a practical day-by-day Singapore travel plan.
If the dates suggest a trip length, use that length.
If not, infer a reasonable number of days from the travel dates mentioned by the user.

Include:
- Attractions
- Food suggestions
- Transport guidance
- Daily schedule

Make it practical and enjoyable.
"""

final_response = client.chat.completions.create(
    model=MODEL,
    messages=[
        {"role": "system", "content": final_prompt}
    ]
)

print("\n===== FINAL ITINERARY =====\n")
print(final_response.choices[0].message.content)

# Try input departure date earlier than arrival date - no validation check