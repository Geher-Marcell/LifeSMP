# LifeSMP (HUN)

**A PLUGIN PAPER 1.20 SZERVERHEZ LETT KÉSZÍTVE** [Letöltés](https://papermc.io/downloads/all) <br>
A világ seedje amihez készült: `-18782750758146629`

## Játékleírás
A Life SMP egy olan minecraftos végigjátszás ahol a játékosoknak 30 szívük van és 6 életük. A játék során regenerálódásra nincs lehetőség csak ölés által.
A játékosok életek alapján vannak kategorizálva:
- 💚 6-5 életen zöld
- 💛 4-3 életen sárga
- ❤️ 2-1 életen piros

Ha valaki kifogy az életekből akkor kiesett a játékból!
Minden ölésért 15 szív jár, de 30 szív fölé nem tudnak menni a játékosok.

#### __Köcsögfilkó__
Minden session elején kisorsolásra kerül a Köcsögfilkó, akinek az a feladata, hogy megöljön valakit abban a sessionben. Köcsögfilkóként, legyen az illető bármilyen színű, bárkit támadhat. Amennyiben sikeresen megöl valakit, az áldozat +1 életet veszít, a Köcsögfilkó pedig nyer 1 egész életet. Ha nem sikerül senkit sem megölnie, a következő sessiont 2-vel kevesebb élettel fogja kezdeni.

#### __Fontos változtatások__
  - A potik kevesebb ideig tartanak
  - A TNT nem rombol, de ugyanúgy sebez
  - Az összes falusinak custom tradejei vannak, leárazások és áremelések nincsenek

#### __Endgame eventek__
  - World Border: 30 perc alatt lecsökken 30x30 blokkra, ha úgy ítéljük meg, hogy itt az ideje a Végjátéknak
  - Lava Rising: amint 30x30 blokkra lecsökkent a border, egy bizonyos y szintig elkezd nőni a láva a világ aljáról

#### __Sessionok ideje__
  Minden session 3 órás egy 15 perces szünettel a felénél amikor mindenkit kirúg a szerverről

## __Kezelési útmutató__
A plugin nem lett teljes mértékben felhasználó barát módon megcsinálva ezért néhany dolog megváltoztatásához újra kell buildeni.
Ehhez az intellij idea programot tudom ajánlani [Letöltés](https://www.jetbrains.com/idea/download/)

### Parancsok
- `addhealth [játékosnév] [mennyiség]: hozzáad a játékosnak mennyiségnyi szivet. Ha elvenni akarunk akkor minuszt kell megadni mennyiségnek
- `addlife [játékosnév] [mennyiség]: hozzáad a játékosnak mennyiségnyi életet. Ha elvenni akarunk akkor minuszt kell megadni mennyiségnek
- `game`
  - `start`: elindítja a visszaszámlálót
  - `stop`: leállítja a visszaszámlálót
  - `pause`: szünetelteti a visszaszámlálót
  - `resule`: folytatja a visszaszámlálást
  - `settime [másodperc]`: átállítja a visszaszámláló idejét
  - `addtime [másodperc]`: hozzáad a visszaszámláló idejéhez

### __Configfile__
A Configfájlt a plugin első futtatása után generálja a szerver. A `/plugins/Seaseon2/config.yml`-t kell megnyitni.
Itt lehet az alábbiakat szerkeszteni:
  - Falusiak tradeje
  - A poti effektusok osztási hányadosát
  - A játékosok jelenlegi életének számát

### A kódban
_A kódban szinte bármit lehet szerkeszni ha értesz hozzá, de ha nem akkor könnyen használhatatlanná válhat a játék!_
Ajánlott változtatások:
1. `src/main/java/l10/dev/main/Seaseon2.java`
    - `int maxHealth`: a játékosok max életét állítja át. Minden érték egy fél szívnek felel meg!
    - `String prefix`: a plugin által kiírt üzeneteknek a prefixe (az üzenet előtt lévő felírás)
2. `src/main/java/l10/dev/main/KocsogFilko.java`
    - `int penaltyLife`: Az élet amit elveszünk a boogeymantől a játék végén **ha** nem tudott megölni senkit
    - `int rewardLife`: Az élet amit elveszünk az áldozattól és a boogeymannek adjuk **amikor** megöl valakit
    - `int countdown`: A boogeyman kiválasztásához visszaszámláló
3. `src/main/java/l10/dev/main/Game.java`
    - `int sessionLength`: A játéknak(session) hossza másodpercekben
    - `int getPauseLength()`: a szünetnek a hossza másodpercekben
4. `src/main/java/Commands/StartLava.java`
    - `int centerX`: A játéktér közepének az X koordinátája
    - `int centerZ`: A játéktér közepének a Z koordinátája

## Fejlesztőknek
Ha valami arra vinne, hogy továbbfejleszd ezt a plugint akkor az egyetlen kérésem, hogy valami módon jelöljetek meg benne
