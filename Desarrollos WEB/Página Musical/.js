var audio;

document.addEventListener('play', function(e)
{

    var audio = document.getElementsByTagName('audio');


    for(var i = 0, len = audio.length; i < len;i++)
    {
        if(audio[i] != e.target)
        {
            audio[i].pause();
            //audios[i].src = "";  //por ejemplo aquí no lo puedo usar        
        }
    }           

}, true);