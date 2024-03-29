# Hangman
Õpilastele jagamiseks täienduste tegemiseks

## Mängu Hangman funktsionaalsuse lisamine
Mängus on graafiline osa kõik tehtud ning lisaks on ka osa funktsionaalsust olemas. Näiteks on vaadatav edetabel ja arvatavad sõnad koos 
kategooriatega teada. Vastavad nupud **Edetabel** ja **Sõnad**. Osaliselt on hetke seisuga tööle pandud **Uus mäng** ning **Katkesta mäng**. Viimane 
neist peaks olema täisfunktsionaalsuses hetke seisuga. **Katkesta mäng** nupp tuleb nähtavale, kui vajutatakse Uus mäng nuppu. Ja see nupp 
peab kaduma, kui mäng lõppeb või mäng katkestatakse. 

## NB!
~~**Refactor, projekti nimeks Hangman_Eesnimi, kus eesnimi on sinu eesnimi ilma täpitähtedeta ja tühikuteta!**~~


## Funktsionaalsus
~~**Uus mäng** – Vastavalt kasutaja kategooria valikule tuleb valida tabelist/Listist (_kumb on mugavam_) juhuslik sõna. Kui valikuks 
on**Kõik kategooriad**, siis kõikide sõnade seast, kui aga kindel kategooria, siis ainult selle kategooria sõnade seast. Kui sõna on teada, algab
mäng. Uut sõna kus iga täht on asendatud all kriipsuga näidata wordPanel-il oleval sildil (JLabel). Iga tähe (all kriipsu) vahel on ka 
tühik.~~

~~**Saada täht –** Kui lahtrisse **Sisesta täht** on täht sisestatud ja nupule **Saada täht** (_k.a. Enter klahvi vajutus_) vajutatud, siis 
tehakse kontroll. Kas täht on arvatavas sõnas olemas või mitte. Kui sisestatud täht on sõnas olemas, siis kõik need tähed sõnas tulevad 
kasutajale nähtavale. Tähed on TRÜKITÄHTEDEGA. Teadmata tähed on all kriipsud (_). Iga puuduv täht on üks all kriips. Nupp Saada täht 
töötab ka Enter klahvi vajutusega!~~
Ma lisasin selle, et kui sõna keskel on täht, mis kattub esimese või viimase tähega, siis seda kinni ei kaeta. 
Pole küll nõutud, aga mina mäletan, et hangmanis oli nii.

~~Kui tähte ei olnud sõnas, siis loetakse kokku mitmes mööda pandud täht oli. Lisaks jäetakse täht meelde (SUURTÄHENA). Kui mööda on pandud 
7 või rohkem tähte, siis lõppeb mäng ära.~~ ~~Kasutajat edetabelisse ei lisata. Kui kasutaja arvab sõna ära enne seitset sammu, siis küsitakse 
temalt nime (nimi vähemalt 2 tähte pikk) ning vajalik info lisatakse edetabelisse.~~

~~Kui tähega pandi mööda, siis tekst **Valesti 0 täht(e).**, number kasvab ning peale punkti näidatakse valesti sisestatud tähti 
trükitähtedega eraldatud komadega.~~ ~~Sama tähtede rida läheb ka edetabelisse,~~ ~~kui saadakse all 7 vale täheta hakkama. Kui esimene täht 
valesti läheb, siis JLabel, mis vigade infot näitab läheb punaseks. Kui mäng saab läbi, siis muutub tekst jälle mustaks.~~  
~~Nime küsimiseks kasuta **JOptionPane.showInputDialog** lahendust. Kui kasutaja nime ei sisesta, siis kasutajat ka edetabelisse ei lisata.~~
~~Kui kasutaja kirjutab alla 2 märgise nime, siis ka kasutajat edetabelisse ei lisata. Lisaks antakse kasutajale ka teada, et vigase info 
puhul teda edetabelisse ei lisata (tagant järgi).~~
Võtsin vaba voli selle pisut teistmoodi teha. Kasutajale antakse võimalus uuesti sisestada ja antakse talle teada, et peab kaks tähemärki olema.
~~**Katkesta mäng** - kui seda klikitakse, siis kedagi kuhugi ei lisata. Seadistatakse algseis, et saaks alustada uut mängu (klikkida nupul **Uus mäng**).~~
See võimekus oli ka kohe alguses olemas

## Mis info on vaja lisada edetabeli faili?
~~Hetke kuupäev koos kellaajaga kujul AAAA-KK-PP HH:MM:SS. Antud tabel tahab seda aega sõnena. Mängija nimi sõnena. Sõna mida arvati. 
Tähed mis valesti sisestati, eraldatud komaga ja SUURTÄHED. Viimane veerg võib ka tühjaks jääda, muud veerud peavad tabelis täidetud 
olema.~~

## Lisa võimalused
1. Kategooria, kus on alla mingi arvu sõnu, ei saa valida. <-- seda ma ei tee, sest tundub minu jaoks ebaloogiline. neid ei peaks siis ju kasutajale kuvamagi.
2. ~~Tee Edetabeli lahtrid mitte muudetavaks.~~
3. ~~Tee Sõnade tabeli lahtrid mitte muudetavaks.~~

## Asjad, mis Margus teistmoodi tegi
1. Kui sõna keskel on samu tähti, mis esimene või viimane, siis jäetakse ka need avatuks
2. Kui lahtrisse midagi kirjutatud ei ole, siis  "Saada" nupp on mitteaktiivne
3. Kui kasutaja sisestab nimeks vähem, kui kaks tähemärki, siis tuleb INFORMATION_MESSAGE ja võimalus uuesti sisestada.
4. Pakkuda saab ainult tähti. <-- Selle teen pisut hiljem.

