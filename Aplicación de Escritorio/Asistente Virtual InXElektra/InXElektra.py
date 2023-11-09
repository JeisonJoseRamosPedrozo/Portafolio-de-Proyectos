import pywhatkit
import speech_recognition as sr
import webbrowser
import requests
from bs4 import BeautifulSoup
import wikipedia as wiki
import pyttsx3
import cv2
import tkinter as tk
import time
import wikipedia
import pyglet
import threading
from queue import Queue

# Configura el avatar
name_avatar_bot = "InXElektra"

# Lista de palabras a ignorar
palabras_a_ignorar = ["busca", "buscar", "búscame", "encuentra", "encontrar", "encuéntrame", "muestra", "muéstrame", "mostrar", "abre", "abrir", "dime el"]

# Configura el reconocimiento de voz
r = sr.Recognizer()

boca_activa = False  # Inicialmente, la boca no está activa

# Función para activar o desactivar la boca
def set_mouth_state(active):
    global boca_activa
    boca_activa = active

# Cola para controlar la boca
boca_queue = Queue()

def activate_mouth():
    boca_queue.put(True)

# Función para desactivar la boca
def deactivate_mouth():
    boca_queue.put(False)

# Función para escuchar comandos de voz con un tiempo de espera
def listen_for_command(timeout=10):
    with sr.Microphone() as source:
        set_mouth_state(True)  # Activa la boca antes de escuchar
        speak("Dime qué quieres saber, hacer o buscar:")
        r.adjust_for_ambient_noise(source)  # Ajustar para reducir el ruido ambiental
        try:
            audio = r.listen(source, timeout=timeout)
            command = r.recognize_google(audio, language="es-ES")
            return command
        except sr.WaitTimeoutError:
            speak("Tiempo de espera agotado. Puedes hablar de nuevo.")
            time.sleep(10)
        except sr.UnknownValueError:
            speak("¿Disculpa?, no entiendo a que haces referencia")
        except sr.RequestError as e:
            speak(f"Error en la solicitud de reconocimiento de voz: {e}")
        finally:
            set_mouth_state(False)  # Desactiva la boca después de escuchar
    return None

# Función para abrir el primer resultado de búsqueda en Google
def open_first_search_result(query):
    google_search_url = f"https://www.google.com/search?q={query}"
    response = requests.get(google_search_url)
    soup = BeautifulSoup(response.text, 'html.parser')
    search_results = soup.find_all('a')
    for result in search_results:
        href = result.get('href')
        if href.startswith('/url?'):
            url = href.split('?q=')[1].split('&')[0]
            webbrowser.open(url)
            break

# Modificada función "speak" para controlar la boca
def speak(text):
    engine = pyttsx3.init()

    activate_mouth()  # Activa la boca antes de hablar
    engine.say(text)
    engine.runAndWait()
    deactivate_mouth()  # Desactiva la boca después de hablar

# Función para procesar y responder a comandos
def process_command(command, message_queue):
    if command:
        # Separa las palabras del comando
        palabras = command.split()
        # Filtra las palabras a ignorar
        palabras_filtradas = [palabra for palabra in palabras if palabra.lower() not in palabras_a_ignorar]
        # Reconstruye el comando sin las palabras a ignorar
        comando_procesado = " ".join(palabras_filtradas)
        comando_procesado = comando_procesado.strip()  # Elimina espacios adicionales
        if comando_procesado:
            if any(keyword in comando_procesado for keyword in ["video", "ver un video"]):
                keywords = ""
                for keyword in ["video", "ver un video"]:
                    if keyword in comando_procesado:
                        keywords = comando_procesado.split(keyword, 1)[1].strip()
                        break
                if keywords:
                    speak("Por supuesto, te mostraré un video de " + keywords)
                    pywhatkit.playonyt(keywords)
            elif "Cómo te llamas" in comando_procesado:
                speak("Yo me llamo, " + name_avatar_bot)
            elif "Qué significa tu nombre" in comando_procesado:
                speak(name_avatar_bot + " es una combinación de las palabras 'inteligencia y 'electrónica', significa que estoy relacionada con la IA y la Tecnología avanzada, para propósitos futuros supondré una fusión de procesos mentales con componentes eléctricos y cibernéticos, con lo que mi nombre terminará de adquirir completo sentido")
            elif "gracias" in comando_procesado or "muchas gracias" in comando_procesado:
                speak("No hay de que")
            elif "en YouTube" in comando_procesado:
                speak("Claro que sí, te suministraré la lista de reproducción de videos sobre " + comando_procesado)
                # Si el usuario menciona "en YouTube" en relación al tema, abre la lista de videos de la búsqueda de YouTube
                keywords = comando_procesado.replace("en YouTube", "").strip()
                youtube_search_url = f"https://www.youtube.com/results?search_query={keywords}"
                webbrowser.open(youtube_search_url)
            elif "Quiero información de" in comando_procesado or "Dime información de" in comando_procesado or "Dime quién es" in comando_procesado or "Dime quién era":
                palabras_clave = ["Quiero información de", "Dime información de", "Dime quién es", "Dime quién era"]
                clave_encontrada = None
                for clave in palabras_clave:
                    if clave in comando_procesado:
                        clave_encontrada = clave
                        break

                if clave_encontrada:
                    consulta = comando_procesado.split(clave_encontrada)[-1].strip()
                    if consulta:
                        wikipedia.set_lang("es")
                        try:
                            info = wiki.summary(consulta)
                            speak(info)
                        except wikipedia.exceptions.DisambiguationError as e:
                            speak("Tengo múltiples posibilidades distintas para contestarte acerca de"+ consulta +". Por favor, sé más específico para saber que contestarte exactamente.")
                        except wikipedia.exceptions.PageError as e:
                            speak(f"No tengo ninguna información acerca de '{consulta}'. Te sugiero ser mas específico en la combinación de palabras para así lograr proporcionarte información")
                    else:
                        speak("Lo siento, no me has proporcionado un tema válido, el tema podría estar restringido.")

                            
            elif any(keyword in comando_procesado for keyword in ["página", "página web", "sitio", "sitio web", "web", "dirección", "dirección web"]):
                speak("No hay problema, mira este sitio web que he conseguido sobre " + comando_procesado + "para ti")
                # Abre el primer resultado de búsqueda en Google relacionado al tema si menciona algunas de las palabras claves
                open_first_search_result(comando_procesado)
            else:
                speak("Aquí tienes la lista de los resultados de tu búsqueda acerca de " + comando_procesado)
                # Realiza una búsqueda en Google en lugar de abrir una página web directamente
                google_search_url = f"https://www.google.com/search?q={comando_procesado}"
                webbrowser.open(google_search_url)

# Función para iniciar el bot
def start_bot(message_queue):
    while True:
        command = listen_for_command()
        if command:
            process_command(command, message_queue)

# Función para iniciar Pyglet en un hilo separado
def start_pyglet():
    gif_path = 'componentesVisuales/InXElektra.gif'
    animation = pyglet.image.load_animation(gif_path)

    boca_path = 'componentesVisuales/boca3-unscreen.gif'
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
    message_queue = Queue()

    # Inicia el hilo del chatbot
    chatbot_thread = threading.Thread(target=start_bot, args=(message_queue,))
    chatbot_thread.start()

    # Inicia el hilo de Pyglet
    pyglet_thread = threading.Thread(target=start_pyglet)
    pyglet_thread.start()
