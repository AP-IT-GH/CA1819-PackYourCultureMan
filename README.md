# Information about the project

## Algemene beschrijving

### PacYourCultureMan

![logo](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/logo.png?raw%20=%20true)

PacYourCultureMan is een map-based city-explorer dat zicht afspeelt in de stad Antwerpen.
De naam van het spel geeft een hint naar het populaire pacman, je zal dus ook door een aantal vijanden achtervolgd worden, die je op slinkse wijze om de tuin zal moeten leiden.
Het doel van het spel is zoveel mogelijk locaties bezoeken en onderweg de juist route nemen om zo alle ‘bolletjes’ (punten) te verzamelen en niet te sterven door de spookjes.
 
Des te meer bolletjes je verzamelt, des te hoger zal je score zijn. Je kan je punten gebruiken om powerups en levens te kopen in de shop.
Je kan jezelf verdedigen door ammo te kopen voor de powerups en je zo verweren tegen de spookjes.
Uiteraard zal het spel je score en de locaties waar je al geweest bent onthouden en zullen deze zichtbaar zijn in de app zelf. Hier kun je uiteraard ook de scores van je vrienden opzoeken en kijken wie er bovenaan staat en wat je huidige ranking is. 

Je zal als User een account moeten maken waarmee je in het spel kan inloggen. nadat je dit hebt gedaan zal er een ‘random’ locatie in antwerpen gezocht worden waar je nog nooit geweest bent. Je zal met je character op een map (gebaseerd op google maps) gezet worden. Bovenaan de app staat er wat je random locatie is, hoeveel meter je verwijderd bent van de locatie en in welke richting je moet wandelen om bij je locatie te geraken. Wanneer jij op ‘go’ drukt zal er een timer afgaan, wanneer deze timer ten einde loopt zullen er vijanden op de kaart gezet worden die jij zal moeten ontwijken of doden. Wanneer je je bezienswaardigheid hebt gevonden, heb je je punten verdiend en kan je deze inzetten in de shop. Je zal bijkomende informatie krijgen bij elke bezienswaardigheid die je bezoekt.

### Screens
![screen1](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/login.jpg?raw%20=%20true)![screen2](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/register.jpg?raw%20=%20true)![screen3](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/startUp.jpg?raw%20=%20true)

![screen4](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/zoomedOut.jpg?raw%20=%20true)![screen5](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/zoomedInAndGuns.jpg?raw%20=%20true)![screen6](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/shop.jpg?raw%20=%20true)

![screen7](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/leaderboard.jpg?raw%20=%20true)![screen8](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/score.jpg?raw%20=%20true)![screen9](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sights.jpg?raw%20=%20true)


![screen10](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/detailSight.jpg?raw%20=%20true)![screen11](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/skins.jpg?raw%20=%20true)![screen12](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/settings.jpg?raw%20=%20true)


## Installatie Backend

Bij het openen van het ASPCoreApi project moet eerst een deploy worden uitgevoerd op Azure. Ga hiervoor naar de solution explorer
en klik met de rechtermuisknop op het ASPCoreApi project en selecteer Publish. je zal eerst een nieuw profiel moeten aanmaken. Druk dus eerst op New Profile.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc1.PNG?raw%20=%20true)

Kies een gepaste Api naam, selecteer de juiste subscription. maak eventueel een nieuwe recoursegroup aan indien dit nog niet gebeurd is.
Zelfde geldt voor een hostingplan. Voor studenten kies je best de F1 subscriptie.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc2.PNG?raw%20=%20true)

Vervolgens zal de Database moeten worden aangemaakt. klik op create database, zorg voor een gepaste naam. indien je nog geen server hebt om de database op te hosten klik je op new bij het server vak. je kiest ook hiervoor een gepaste naam en een admin username en password.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc3.PNG?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc4.PNG?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc5.PNG?raw%20=%20true)

Als dit gebeurd is kan je op create klikken.
### azure password ophalen

Om het server password te krijgen ga je naar je azure dashboard en ga je naar de resources. Selecteer de App service en druk op publicatie profiel ophalen. open dit bestand met een text editor en haal het password eruit. Je kan hier ook de servernaam en alle andere benodigde info uit halen.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc6.PNG?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc7.PNG?raw%20=%20true)

### ASP deploy configure

In de publish page klik je nu op configure en zal je de nodige gegevens moeten invullen. Ook het password dat je uit het publicatie profiel hebt gehaald.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc8.PNG?raw%20=%20true)

Hierna klik je op Settings en duid klik je op databases dropdown. Je vinkt hier "use this connectionstring at runtime". 
Je klikt nu op Entity framework migrations, en duid "apply this migration on publish" aan. vervolgens klik je op saven.

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc9.PNG?raw%20=%20true)

Als dit allemaal in orde is kan je op publish drukken. Je api is nu gelinkt met de database en kan gebruikt worden door de applicatie.

### Sights inladen Swagger

Omdat we de Sights hardcoded in de Database toevoegen. Zullen deze eerst door de admin moeten worden geinitialiseerd.
Ga hiervoor naar het destination URL, dit kan je vinden bij de configure dialoog uit vorig deel en plaatst hier in de browser /swagger achter.
Je ziet hier alle endpoints.
Selecteer het post Sights/postarray en kopieer de Sights JSON die je vindt in de map doc in de github repo. Plak deze in het value veld en druk op execute. De sights zijn nu toegevoegd.

![doc](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/doc.png?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc10.PNG?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc11.PNG?raw%20=%20true)

![asp](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/sc12.PNG?raw%20=%20true)

### Streets inladen

Omdat we de Streets hardcoded in de Database toevoegen. Zullen deze eerst door de admin moeten worden geinitialiseerd.
Ga hiervoor naar het destination URL, dit kan je vinden bij de configure dialoog uit vorig deel en plaatst hier in de browser /swagger achter.
Je ziet hier alle endpoints.
Selecteer het post Streets/postarray en kopieer de Streets JSON die je vindt in de map doc in de github repo. Plak deze in het value veld en druk op execute. De streets zijn nu toegevoegd.

Nu dat je de streets hebt ingeladen kunnen de dots gemaakt worden. De dots worden gegenereerd door middel van punt a en b en om de 30 meter wordt er een nieuw punt tussen deze 2 punten aangemaakt, deze worden door Google Roads Api gecorrigeerd en op de weg gezet.

Om deze dots te genereren moet je in de database onder user het acceslevel op true (1) plaatsen en dan zal in het gameMenu de optie dev beschikbaar zijn.

Als je de streets hebt ingeladen zal de update knop in de devoptions alles updaten en zal de lijst dots volgende keer als je inlogd worden ingeladen.

![acceslevel](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/acceslevel.png?raw%20=%20true)
![doc](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/doc.png?raw%20=%20true)
![swagger](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/swaggerStreet.png?raw%20=%20true)
![screen13](https://github.com/AP-Elektronica-ICT/CA1819-PackYourCultureMan/blob/master/img/devOptions.jpg?raw%20=%20true)
