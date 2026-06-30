from fastapi import FastAPI

from routers.chat_router import router as chat_router


app = FastAPI(title="Car Rental LLM Service")

app.include_router(chat_router)


@app.get("/")
def root():
    return {
        "message": "FastAPI LLM service is running. Open the Spring Boot app at http://localhost:8080"
    }


if __name__ == "__main__":
    import uvicorn

    uvicorn.run("main:app", host="127.0.0.1", port=8000, reload=True)
