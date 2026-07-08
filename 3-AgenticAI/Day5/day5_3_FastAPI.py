from fastapi import FastAPI
from pydantic import BaseModel

from day5_3_MCP_agent import ask_agent


app = FastAPI(
    title="MCP Agent API",
    description="Expose LangChain MCP agent through FastAPI",
    version="1.0"
)

class ChatRequest(BaseModel):
    question: str

class ChatResponse(BaseModel):
    answer: str


@app.get("/")
def home():
    return {"message": "MCP Agent API is running"}


@app.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    answer = await ask_agent(request.question)
    return ChatResponse(answer=answer)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8080)