# CornerGame

„Cornern“, neudeutsch, bezeichnet das gemeinsame Abhängen an der Straßenecke, vorzugsweise am Kiosk. Höchste Zeit ein Spiel daraus zu machen: Mit dem CornerGame bekommt die Kioskkultur eine eigene Plattform, in der User Kioske sammeln, an Quests teilnehmen und die Straßenecken der Stadt erkunden können. Es gibt viel zu entdecken!

The CornerGame is a community-based online game. Players can collect Kiosks in Hamburg by checking in with their geolocation and gain points. Additionally there are quests: by collecting Kiosks along a given route within a time limit Users can get additional points.

![CornerGame_ScreenshotIPadAir](https://user-images.githubusercontent.com/93976072/183520483-532e4822-a730-446c-bf5e-46570de3797f.png)

## Used technologies
* Backend: Spring, Java, Maven, MongoDB, Docker
* Frontend: React, ReactRouter, TypeScript, Google Maps API, HTML5, CSS3

## Special features
For better expandability and easier testing the backend is split into several microservices which are interconnected by the observerpattern. On the frontend of the application Google Maps API and Google Directions API are embedded as a component that can be controlled by a simple menu-component-overlay. The application is optimized for mobile use.

https://user-images.githubusercontent.com/93976072/183519890-08bf7d5b-08b0-4d70-a4c5-eb2bea623737.mp4

coming soon to: www.cornergame.de
