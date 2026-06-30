# Input: Collects arrival, departure, budget
# Generate Itinerary
# Use LLM to validate dates -- not reliable
# stops if invalid

from openai import OpenAI
import os

client = OpenAI(
    base_url="https://openrouter.ai/api/v1",
    api_key=os.getenv("OPENROUTER_API_KEY")
)

MODEL = "meta-llama/llama-3-8b-instruct"

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

# Ask the LLM to validate the dates
validation_prompt = f"""
Check whether these travel dates are valid.

Arrival date: {memory["arrival"]}
Departure date: {memory["departure"]}

Rules:
- If the departure date is earlier than the arrival date, reply with exactly: INVALID
- If the dates look valid, reply with exactly: VALID
- Do not explain anything else
"""

validation_response = client.chat.completions.create(
    model=MODEL,
    messages=[
        {"role": "system", "content": validation_prompt}
    ],
    temperature=0
)

validation_result = validation_response.choices[0].message.content.strip().upper()

if validation_result != "VALID":
    print("\nError: departure date may be earlier than arrival date.")
    print("Please run the program again and enter valid dates.")
    exit()

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