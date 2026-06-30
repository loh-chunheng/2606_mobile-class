from fastapi import APIRouter, HTTPException
from pydantic import BaseModel

from services.chatbot import generate_reply

router = APIRouter()

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

@router.post("/chat", response_model=ChatResponse)
def chat(request: ChatRequest):
    if request.message is None or not request.message.strip():
        raise HTTPException(status_code=400, detail="Message is required.")

    try:
        reply = generate_reply(request.message)
    except Exception as error:
        raise HTTPException(status_code=500, detail=str(error)) from error

    return ChatResponse(reply=reply)
