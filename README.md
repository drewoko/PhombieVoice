# PHombie Voice

Simple application for requesting throw http requests and playing audio throw browser. 
Can we used for generating audio directly from Ivona Cloud.

As Ivona Cloud have very unclear pricing policy, this application uses trial period of it - using key rotation.

## Requirements
* JDK 1.8
* Ivona Cloud API keys

## Configuration
### Options
* **-h** or **--help** - printing help
* **-ie** or **--ivona-entry** *arg* - Ivona Cloud Entry-point (Default: https://tts.us-east-1.ivonacloud.com)
* **-ik** or **--ivona-keys** *arg* - File with Ivona cloud API keys (Required)
* **-iv** or **--ivona-voice** *arg* - Default Ivona Voice ([List of voice](https://goo.gl/1vk7xq))
* **-p** or **--web-port** *arg* - HTTP service port (Default: 8080)
* **-pi** or **--play-immediately** *arg* - Play audio immediately when request sent or using queue (Default: true)
* **-ps** or **--play-speed** *arg* - Audio playback speed (Default: 1.0)
### Ivona keys file format
```json
[
    {"AWSAccessKeyId": "XXXXXXXXXXXX1", "AWSSecretKey": "XXXXXXXXXXXXXXXXXXXXXXXXX1"},
    {"AWSAccessKeyId": "XXXXXXXXXXXX2", "AWSSecretKey": "XXXXXXXXXXXXXXXXXXXXXXXXX2"}
]
```

## HTTP endpoints
### GET /
Root endpoint that loads html page, with code, that receives requests from /ws endpoint and plays audio throw /play endpoint.
### WebSocket /ws 
WebSocket endpoint. Receives requests that routed from /request endpoint.
#### Message format
```json
{
  "text": "Requested to play text", 
  "speed": "1.0",
  "voice": "Maksim",
  "immediately": true
}
```
### GET /request
Endpoint that pass request to /ws endpoint using request string.
* **text** - request text (required)
* **speed** - audio playback (optional, will be taken from options)
* **voice** - Ivana voice (optional, wll be taken from options)
* **immediately** - Play audio immediately when request sent or using queue (optional, wll be taken from options)
### GET /play
Endpoint that returns generated audio in OGG format.
* **text** - request text (required)
* **voice** - Ivana voice (optional, wll be taken from options)
