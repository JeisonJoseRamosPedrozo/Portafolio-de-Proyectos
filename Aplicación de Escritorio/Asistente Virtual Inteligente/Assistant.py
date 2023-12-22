from bardapi import Bard
import os
from dotenv import load_dotenv
import speech_recognition as sr
import pyttsx3
import threading
import pyglet
from queue import Queue

name_avatar_bot = "InXElektra"

load_dotenv()

token = os.getenv("BARD_API_KEY")

bard = Bard(token=token)

recognizer = sr.Recognizer()

boca_activa = False  # Inicialmente, la boca no está activa
boca_queue = Queue()

def activate_mouth():
    boca_queue.put(True)

def deactivate_mouth():
    boca_queue.put(False)

def set_mouth_state(active):
    global boca_activa
    boca_activa = active

def speak(text):
    engine = pyttsx3.init()
    activate_mouth()  # Activa la boca antes de hablar
    engine.say(text)
    engine.runAndWait()
    deactivate_mouth()  # Desactiva la boca después de hablar

def get_audio():
    recognizer = sr.Recognizer()
    with sr.Microphone() as source:
        set_mouth_state(True)  # Activa la boca antes de escuchar
        speak("Por favor, haz tu pregunta...")
        recognizer.adjust_for_ambient_noise(source)
        try:
            audio = recognizer.listen(source)
            text = recognizer.recognize_google(audio, language="es-ES")
            return text
        except sr.UnknownValueError:
            speak("No se pudo entender la pregunta.")
        except sr.RequestError:
            speak("Error en la solicitud.")
        finally:
            set_mouth_state(False)  # Desactiva la boca después de escuchar
    return None

def get_bard_response(message_queue):
    while True:
        question = get_audio()

        if question:
            try:
                result = bard.get_answer(question)
                content = result.get('content', '')
                if content:
                    speak(content)
                else:
                    speak("Lo siento, no se encontró una respuesta adecuada.")
            except Exception as e:
                speak(f"Hubo un error al obtener la respuesta: {str(e)}")
        else:
            print("No se pudo entender la pregunta. Por favor, inténtalo de nuevo.")

def start_response_thread():
    message_queue = Queue()
    response_thread = threading.Thread(target=get_bard_response, args=(message_queue,))
    response_thread.start()

# Función para iniciar Pyglet en un hilo separado
def start_pyglet():
    gif_path = 'InXElektra.gif'
    animation = pyglet.image.load_animation(gif_path)

    boca_path = 'boca3-unscreen.gif'
    boca_animation = pyglet.image.load_animation(boca_path)

    window = pyglet.window.Window(width=400, height=400)

    anim_sprite = pyglet.sprite.Sprite(animation, x=0, y=0)

    boca_sprite = pyglet.sprite.Sprite(boca_animation, x=0, y=0)

    scale_factor = 0.875
    scale_factor_boca = 0.154

    @window.event
    def on_draw():
        window.clear()
        
        global boca_activa
        
        if not boca_queue.empty():
            boca_activa = boca_queue.get()

        anim_sprite.scale = scale_factor
        dx = (window.width - anim_sprite.width) / 2
        dy = (window.height - anim_sprite.height) / 2
        
        anim_sprite.x = dx
        anim_sprite.y = dy
        
        anim_sprite.draw()
        
        if boca_activa:
            dx_boca = (anim_sprite.width - boca_sprite.width) / 2 + 62.6
            dy_boca = (anim_sprite.height - boca_sprite.height) / 2 + 12.5

            boca_sprite.x = dx_boca
            boca_sprite.y = dy_boca

            boca_sprite.scale = scale_factor_boca

            boca_sprite.draw()

    pyglet.app.run()


if __name__ == "__main__":

    # Inicia el hilo del chatbot
    response_thread = threading.Thread(target=start_response_thread)
    response_thread.start()

    # Inicia el hilo de Pyglet
    pyglet_thread = threading.Thread(target=start_pyglet)
    pyglet_thread.start()
