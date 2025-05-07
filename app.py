from fastapi import FastAPI
from pydantic import BaseModel
import numpy as np
import joblib

app = FastAPI(title="Predicción de Calidad del Aire")

scaler = joblib.load("scaler3.joblib")
aqi_encoder = joblib.load("aqi_mapping.joblib")
hour_encoder = joblib.load("encoded_hour.joblib")
model = joblib.load("best_rf.joblib")

category_map = {
    0: ("Bueno", "😊", "El aire es excelente para cualquier actividad al aire libre."),
    1: ("Moderado", "😐", "El aire es aceptable, pero algunas personas sensibles pueden experimentar molestias."),
    2: ("No sano para grupos sensitivos", "😷", "Niños, ancianos y personas con problemas respiratorios deberían limitar el ejercicio al aire libre."),
    3: ("No sano en general", "🤒", "Se recomienda evitar actividades intensas al aire libre."),
    4: ("Muy poco sano", "🤢", "Ejercicio al aire libre no recomendado. Riesgo alto."),
    5: ("Peligroso", "☠️", "Evite cualquier actividad al aire libre."),
}

class InputData(BaseModel):
    hour: int
    pm25: float
    pm10: float

@app.post("/predict/")
def predict_air_quality(data: InputData):
    scaled = scaler.transform([[data.pm25, data.pm10]])
    features = np.concatenate(([data.hour], scaled[0])).reshape(1, -1)
    prediction = int(model.predict(features)[0])
    category, emoji, advice = category_map.get(prediction, ("Desconocido", "❓", "Información no disponible."))
    return {
        "predicted_category": category,
        "emoji": emoji,
        "advice": advice
    }
