import pyttsx3

def set_voice(engine, voice_id):
    engine.setProperty('voice', voice_id)

# Identificación de la voz que deseas usar (cambia esto por la identificación de la voz que prefieras)
selected_voice_id = "HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Speech\Voices\Tokens\TTS_MS_ES-ES_HELENA_11.0"

# Inicializar el motor de texto a voz
engine = pyttsx3.init()

# Establecer la voz seleccionada
set_voice(engine, selected_voice_id)

# Prueba con un texto para comprobar que se está utilizando la voz seleccionada
engine.say("Hola, esta es una prueba utilizando la voz seleccionada.")
engine.runAndWait()
