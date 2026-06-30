"""
02_keyword_search_simple.py

Goal:
Show very simple keyword search over 5 car records.

This is not smart search. It only checks whether query words appear
inside each car text.

Run:
    python 02_keyword_search_simple.py
"""

from pathlib import Path


DATA_FILE = Path(__file__).parent / "car rental data" / "cars.txt"


def load_cars():
    cars = []
    lines = DATA_FILE.read_text(encoding="utf-8").splitlines()

    for line in lines:
        if not line.strip():
            continue
        car_id, car_text = line.split("|", 1)
        cars.append((car_id, car_text))

    return cars


def keyword_search(query, cars):
    query_words = query.lower().split()
    results = []

    for car_id, car_text in cars:
        car_text_lower = car_text.lower()

        # Count how many query words appear in this car text.
        score = 0
        for word in query_words:
            if word in car_text_lower:
                score += 1

        if score > 0:
            results.append((score, car_id, car_text))

    results.sort(reverse=True)
    return results


cars = load_cars()
# query = "SUV"
query = "which car should i rent"
results = keyword_search(query, cars)

# hardcode query
print("Query:", query)
print()


for score, car_id, car_text in results:
    print("Source ID:", car_id)
    print("Score:", score)
    print("Text:", car_text)
    print()

# prompt for input
query = input("Enter vehicle type: ")

for score, car_id, car_text in results:
    print("Source ID:", car_id)
    print("Score:", score)
    print("Text:", car_text)
    print()

