# Cocktailapp - Aplikacja mobilna

## Projekt: Lista-Szczegół: przepisy koktajli

## Prezentacja działania:

https://youtu.be/GHc4hViKelc

## Autor: Szymon Warguła 158037

## Semestr: 6

## Użyte technologie:
- Środowisko: Android Studio
- Język: Kotlin
- UI: Jetpack Compose + Material Design 3
- Baza danych: Room
- Parser JSON: Moshi
- Obsługa danych: cocktails.json

## Aplikacja działa prawidłowo dla urządzeń z systemem Android w wersji API 24+.

## Uwagi techniczne:
- brak użycia fragmentów
- pliki obrazków przechowywane lokalnie
- brak zewnętrznych połączeń sieciowych
- nie można edytować bazy danych

## Zaimplementowane funkcje:
- lista nazw koktajli, szczegółów (nie można edytować) i strona główna
- nie użyto fragmentów
- minutnik
- Działa poprawnie po zmianie orientacji
- Interfejs dostosowujący się do typu urządzenia: wersja Split View dla tabletów
- Lista koktajli z podziałem na kategorie, gdzie nawigacja odbywa się za pomocą gestu
przeciągania
- Fab (Floating Action Button) umożliwiający wysyłanie składników przez SMS
- zmiana motywu
- Przewijanie ekranów w pionie oraz przewijany poziomo HorizontalPager z zakładkami
- prosta animacja podczas uruchamiania aplikacji

## Szczegóły implementacji:

Dane przechowywane są w lokalnej bazie danych Room. Źródłem danych przy pierwszym
uruchomieniu jest plik cocktails.json, umieszczony w katalogu assets.
- id: unikalny identyfikator
- name: nazwa koktajlu
- prepTimeMinutes: czas przygotowania
- ingredients: lista składników
- instructions: instrukcje przygotowania
- imageFileName: nazwa pliku obrazka (znajdującego się w res/drawable)

## Przykładowy wpis w pliku .JSON:
<img width="699" height="338" alt="image" src="https://github.com/user-attachments/assets/a7d5da0d-f575-4c13-a8ef-efbab4450e3a" />

Do zarządzania bazą danych użyto RoomDatabase, z komponentami:

- AppDatabase – tworzy lokalną bazę danych Room; przy migracjach usuwa starą bazę
(wygodne przy statycznej bazie).
- AssetCocktailDataSource – ładuje dane z pliku cocktails.json przy użyciu Moshi (tylko przy
pustej bazie) i zapisuje je do Rooma.
- CocktailDao – definiuje zapytania SQL (select, insert, update).
- CocktailEntity – encja reprezentująca strukturę tabeli.
- CocktailRepository – pośredniczy między bazą a ViewModelami/UI (czyli logika pobierania
danych).
- IngredientsConverter – konwertuje listę składników z/do formatu tekstowego do zapisu w
bazie (Room nie obsługuje list bez konwertera).
Plik CocktailApp dziedziczy po Application, więc uruchamia się raz na początku działania aplikacji i
inicjalizuje bazę danych i ładuje dane z pliku JSON przy starcie aplikacji.

## ViewModele:

**CocktailDetailViewModel** odpowiada za szczegóły wybranego koktajlu — otrzymuje
CocktailRepository (przekazywany np. przez hiltViewModel() lub fabrykę). Udostępnia dane jednego
koktajlu przez StateFlow (pole cocktail). Umożliwia załadowanie konkretnego koktajlu po ID
(loadCocktail) oraz jego aktualizację (updateCocktail). Komunikacja z repozytorium odbywa się w tle
(viewModelScope.launch).

**CocktailListViewModel** odpowiada za listę wszystkich koktajli. Tak jak wyżej, współpracuje z
CocktailRepository. Od razu po utworzeniu (init) pobiera wszystkie koktajle i udostępnia je jako
StateFlow (cocktails). Przechowuje listę typu List<CocktailEntity>, aktualizowaną z bazy danych.

**TimerViewModel** zarządza logiką timera (zliczanie w dół). Pozwala ustawić czas (setTime),
wystartować (start), wstrzymać (pause) i zresetować (reset). Czas i status odliczania są obserwowalne przez StateFlow (remainingSeconds i isRunning). Timer działa asynchronicznie w viewModelScope, z użyciem delay. Po wyczyszczeniu ViewModelu (onCleared) przerywa działający
timer.

<img width="292" height="203" alt="image" src="https://github.com/user-attachments/assets/22c05abe-38f6-4b48-a4e8-256898ef7a8a" />

*Zdjęcie Minutnika z CocktailDetailScreen.

**MainActivity** to główna aktywność - uruchamia interfejs użytkownika i obsługuje przełączanie
motywów (przechowuje stan motywu). Ładuje AppNavHost, który odpowiada za całą nawigację
między ekranami (lista, szczegóły, itd.)

**AppNavHost** odpowiedzialny jest za nawigację w aplikacji – przełączanie między ekranem startowym
(SplashScreen), listą koktajli oraz ekranem szczegółów danego koktajlu. Przekazuje odpowiednie
parametry (np. cocktailId) do kolejnych ekranów i dostosowuje interfejs do wielkości ekranu.

Wykorzystuje funkcję isTablet() (z DeviceUtils), aby rozpoznać, czy urządzenie to tablet – jeśli tak,
wyświetla jednocześnie listę i szczegóły (SplitCocktailScreen); jeśli nie – pokazuje osobne ekrany.

Na starcie aplikacji zawsze inicjalizuje SplashScreen – prostą animację z obracającym się logo
aplikacji (ic_cocktail_splash). Animacja trwa 3 sekundy niezależnie od rzeczywistego czasu ładowania
danych – przejście następuje automatycznie po upływie tego czasu.

**SplitCocktailScreen** otrzymuje z AppNavHost informacje o motywie oraz kontroler nawigacji i
wyświetla po lewej stronie listę z kategoriami i koktajlami, a po prawej szczegóły przepisu (jeśli został
wybrany element z listy). Odpowiada za przekazanie ID wybranego koktajlu do komponentu
CocktailDetailScreen. Ten ekran pojawia się tylko na tabletach (przy szerokości ekranu ≥ 600dp).

**CategoryTabContent** odpowiada za wyświetlenie listy koktajli w danej kategorii w formie kart z
nazwą, czasem przygotowania i zdjęciem (wczytywanym dynamicznie na podstawie nazwy z bazy).
Otrzymuje dane z CocktailListScreen: listę koktajli, kontekst i funkcję obsługującą kliknięcie
(onItemClick), która przekazuje ID wybranego elementu dalej do nawigacji.

**CocktailDetailScreen** ekran prezentujący szczegóły pojedynczego koktajlu
(cocktailId). Łączy dane z CocktailDetailViewModel i zarządza minutnikiem
(TimerViewModel).

- Pokazuje zdjęcie, nazwę, czas przygotowania, składniki oraz instrukcje.
- Umożliwia ustawienie czasu ręcznie i uruchomienie timera (start/pauza/reset).
- Fab do wysyłania listy składników SMS-em (Intent.ACTION_SENDTO).
- Dane koktajlu są ładowane przez LaunchedEffect, a timer automatycznie ustawiany na podstawie prepTimeMinutes.

<img width="345" height="717" alt="image" src="https://github.com/user-attachments/assets/633a7510-20a2-4ac1-9b28-7073ba4c3278" />



**CocktailListScreen** główny ekran z listą koktajli:

- Wykorzystuje CocktailListViewModel do pobierania danych.
- Wykorzystuje HorizontalPager z zakładkami: Home, Łatwe (preptime ≤ 3 min), Trudne (preptime > 3 min).
- Posiada płynne zapętlenie stron (symulacja nieskończonego scrollowania — LaunchedEffect z scrollToPage).
- Obsługuje zmianę trybu ciemnego i przejście do szczegółów przez callback onItemClick.

<img width="349" height="745" alt="image" src="https://github.com/user-attachments/assets/9e57c465-d1ad-416d-91a8-b5d6bea2f8e2" />



**HomeTabContent** zawartość zakładki Home.

- Wyświetla: powitanie i krótki opis aplikacji + autora.
- Umożliwia przełączenie trybu ciemnego (Switch).
- Całość przewijana (verticalScroll) i stylizowana zgodnie z MaterialTheme.

<img width="338" height="683" alt="image" src="https://github.com/user-attachments/assets/d9f7fb3a-34cc-46e3-8560-1fd18513ce71" />

