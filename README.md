### Jak się tu odnaleźć
Ogólnie Jetpack Compose okazał się dosyć fajny, nie trzeba żadnych xml i te layouty się prosto tworzy, często też wystarczy jak poprosisz chat by ci taki `Composable` napisał i tyle. No właśnie, bo głównym kompontentem budulcowym są tutaj funkcje oznaczone `@Composable`, które mogą oznaczać np. jeden ekran, lub jeden komponent w ekranie. 
Bazę danych możesz przenieść w sumie bez zmian, tylko ja tutaj użyłam ViewModel + dependency injection z Hilt. Wydaje mi się, że to nie jest tak istotne, tj. maestro nie powinien się przyczepić że tak to robisz. Więc wracając, mamy tutaj pakiety (zaczynająć omawianie w jakiś najbardziej logiczny sposób)

### Baza danych
```database``` - model danych (`Item`), DAO i Bazę danych
```di``` - od *D*ependency *I*njection, to jest odpowiedzialne za wstrzykiwanie zależności i ogólnie nie wiem co te klasy robią, ale są potrzebne
```repositories``` - repo zależne od dao i tutaj są nowe rzeczy: *MutableLiveData* i korutyny, to realizuje w współbieżność. Ale w stosunku do używania tego repo to nic się nie zmienia, bo dane się aktualizuje poprzez view modele
```viewmodels``` - głównie klasa `HomeViewModel`, ma wstrzykiwane to repo. 

Może teraz coś więcej o tym wstrzykiwaniu, żeby to działało, to oprócz wyżej wymienionego trzeba też stworzyć aplikację - patrz `FoodDiaryApplication` i w MainActivity, które jest 'wejściem' do programu dodać adnotacje i jako zmienną ten view model. I voilà, powinno działać. 

Teraz przechodząc do UI 
```ui.theme``` - zawiera trzy pliki: 
- *Color* : możesz zdefiniować swoje kolory
- *Theme*: tutaj dodałam swój własny theme ale po to, żeby zdefiniować jego dynamiczne zmiany (np. z jasnego na ciemny mode)
- *Type*: czcionki, tu defaultowe

Co jest fajne to to, że możesz się do tych rzeczy odnosić jak normalne zmienne w kodzie poprzez klasę `MaterialTheme`. Ale takie szczegóły już możesz sobie podpatrzeć. 

```views``` - zawierają pliki z funkcjami, które tworzą ekrany. Raczej jest dowolność w tym jak to rozmieścisz bo cała aplikacja składa się głównie z funkcji, nie z klas. 

### UI
Przeanalizujmy kod służący do dodawnia nowej pozycji. Będzie do plik *views/AddEditItemScreen.kt*

```android
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditItemScreen(navController: NavHostController,
                      homeViewModel: HomeViewModel,
                      itemId: Int?,
                      isEdit: Boolean)
```
`@Composable` - właściwie przed każdą funkcją, która będzie tworzyła coś w widoku. Jedna taka funkcja może tworzyć ekran, lub część ekranu. 
`navController` - nawigacja jest bardzo ważna, ale to zaraz
`homeViewModel` - ten argument będzie przekazywany z nawigacji

```android
var title by remember { mutableStateOf(item.title) }
var placeName by remember { mutableStateOf(item.placeName) }
```
To jest deklaracja zmiennej, którą możesz modyfikować i której modyfikacja będzie odświeżać UI


```android
Scaffold(
        topBar = {
            CustomToolbarWithBackArrow(
                title = if (isEdit) "Edit Item" else "Add Item",
                navController = navController
            )
        },
        content = { padding ->
            Surface(
                color = Color.White,
                modifier = Modifier.fillMaxSize()
            ) 
```
`Scaffold` - czasem tego używam, czasem nie, bo tu jak widzisz można przekazać toolbar. *content* to właściwy już ekran. 

No i jak się buduje te UI w ogóle?
Po prostu używasz w większości już gotowych funkcji (Surface, Row, Column), lub piszesz swoje (patrz `ui.theme.customWidgets`). Znajdujesz w internecie lub prosisz chat.

Każda taka funkcja ma mniej więcej składnię

```android
  Column( ... tutaj argumenty dotyczące wyglądu
){
    ... zagnieżdżone komponenty (wywołania funkcji)
}
```
gdzie

```android
Column(
  modifier = Modifier
      .fillMaxSize()
      .padding(padding)
      .verticalScroll(state = scrollState)
)
```
jest opcjonalne, (chyba zadziała też Column {}), ale tutaj zwykle będziesz chciał podać argument `modifier`, który jest tym, czym wcześniej były nieszczęsne uml'e (tak, xml'e, ale uml też jest nieszczęsny). Czyli szerokość, padding itp. poprzez taki *chaining* wywołań. 

### Nawigacja
Zacznijmy od tego, że nawigacja przypomina trochę rest-api, w sensie każdy docelowy ekran ma swój *route* i może mieć parametry. 
Klasa *utils/AppScreens* posiada instancje, które reprezentują ścieżki do każdego z ekranów w aplikacji. Mają tytuł, ścieżkę i ikonę. 

Teraz do pliku *views/AppMainScreen.kt*
`AppMainScreen` jest wywoływane przez `MainActivity`, ale w sumie służy do stworzenia tej nawigacji. 

```android
val onDestinationClicked = { route : String ->
            [...]
            navController.navigate(route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                [...]
            }
        }
```
Komentarz sam się wyjaśnia mam nadzieję (nie ja pisałam ten kawałek jkbc), ale też są inne funkcje `navController` do nawigacji. 

```android
ModalNavigationDrawer(
            [...]
            drawerContent = {

                    ModalDrawerSheet {
                        [...]
                        screens.forEach { screen ->
                            NavigationDrawerItem(label = {Text (text=screen.title)}, selected = false, onClick = { onDestinationClicked(screen.route) })
                            [...]
                        }

                    }
            })
```
Tutaj tworzona jest szuflada z możliwymi nawigacjami.

Ale to samo z siebie wywali błąd, bo ten `navController` nie zna takiej ścieżki w swoim grafie, nie wie jak tam dojść. Za to odpowiada więc

```android
AppRouter(navController = navController, homeViewModel = homeViewModel, appSettingsManager = appSettingsManager, userInfoManager = userInfoManager,
                    openDrawer = { openDrawer() })
```
Nie pytaj dlaczego to tak trzeba, bo ja nie wiem do końca sama, ale działa. 

W *views/AppRouter* mamy funkcję `AppRouter` która odpowiada za routing. 

```android
NavHost(navController = navController, startDestination = AppScreens.ListScreen.route){
        composable(route = AppScreens.ListScreen.route) {
            ListScreen(navController, homeViewModel, openDrawer)
        }

```
Tutaj budujemy graf, który składa się z listy `composable`. Każda z pozycji definiuje ścieżkę, popatrz, że używamy do tego wcześniej wspomnianych klas z *utils/AppScreens* i wywołujemy konkretne ekrany. Zauważ, że przekazujemy też ten *view model* i inne potrzebne parametry, które też mogą stanowić część ścieżki tak jak poniżej

```android
composable(route = AppScreens.ItemDetailsScreen.route + "/{itemId}",
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )) {
            val itemId = it.arguments?.getInt("itemId")
            ItemDetailsScreen(navController, homeViewModel, itemId!!)
        }
```
`navArgument` - argument, w tym przypadku obowiązkowy, z typem i domyślną wartością. Reszta kodu myślę, że jest jasna

```android
 composable(route = AppScreens.AddEditItemScreen.route + "?itemId={itemId}&isEdit={isEdit}",
  [...]
```
To jest ważne o tyle, że te argumenty są opcjonalne, a składnia jest inna. To jak posłużyć się taką nawigacją widać w przykładzie
```android
Button(onClick = {
    navController.navigate(
        AppScreens.AddEditItemScreen.route + "?itemId=" + selectedItem.id.toString() + "&isEdit=" + true.toString())
                 },
  [...]
)
```
w `ItemDetailsScreen` w *views/ItemViews.kt*, gdzie przechodzimy z listy elementów do ekranu konkretnego elementu

### Zmiana preferencji
Nie używa się *SharedPreferences*, ale czegoś takiego jak *dataStore*. Ja tylko zaimplementowałam zmianę motywu jasny/ciemny, ale mniej więcej przedstawię jak to zrobić

1. Zdefiniowanie tkz. *Manager*'a, który też jest argumentem do `@Composable`. Ten manager ma funkcje służące do zmiany preferencji, które są w nim zdefiniowane. Te funkcje sprawiają, że w innych miejscach, które **obserwują** stan którejś z preferencji, odpowiednio zmieni się ich stan.
```android
class AppSettingsManager(context: Context) 
```
W pliku *views/Settings*

3. W odpowiednim `@Composable`, gdzie użytkownik może te preferencje zmieniać najpierw pobierane są aktualne wartości tych preferencji
```android
LaunchedEffect(appSettingsManager.appSettingsFlow) {
        appSettingsManager.appSettingsFlow.collect { appSettings ->
            darkMode = appSettings.darkMode
            [...]
        }
    }
```
`LaunchedEffect` jest chyba współbieżne, idk w sumie. 
3. Następnie w już oddzielnych widget'ach, w odpowiedzi na akcję użytkownika, są one aktualizowane
```android
Switch(
    checked = darkMode,
    onCheckedChange = {
        darkMode = it
        coroutineScope.launch {
            appSettingsManager.updateDarkMode(it)
        }
    }
)
```
`coroutineScope.launch` wywołuje to współbieżnie. 
4. W *viewmodels.ThemeViewModel* jest zdefiniowany model, który może być używany przez inne widoki. Używa `booleanPreferencesKey` lub 
innych by zdobyć wartość preferencji. 
```android
fun request() {
        viewModelScope.launch {
            dataStore.data.collectLatest {
                darkTheme.value = it[forceDarkModeKey]
                fontFamily.value = it[fontFamilyKey]
            }
        }
    }
```
Wywołane przez inny widok pobiera aktualne wartości preferencji. 
5. Ja chciałam zmienić motyw, więc na tą potrzebę zdefiniowałam swój własny motyw w *ui.theme.Theme.kt*
```android
@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {

    val context = LocalContext.current
    val viewModel = remember { ThemeViewModel(context.dataStore) }
    val darkThemeState = viewModel.darkTheme.observeAsState()
    val value = darkThemeState.value ?: isSystemInDarkTheme()

    val fontFamilyState = viewModel.fontFamily.observeAsState()

    LaunchedEffect(viewModel) { viewModel.request() }

    DarkThemeValue.current.value = value
    val colors = if (value) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
```
Mamy *ThemeViewModel*, a następnie zmienną odpowiadającą za motyw `viewModel.darkTheme`. Ten motyw importuję i używam w *MainActivity*.

```android
@Composable
@ReadOnlyComposable
fun isDarkTheme() = DarkThemeValue.current.value

@SuppressLint("CompositionLocalNaming")
private val DarkThemeValue = compositionLocalOf { mutableStateOf(false) }
```
To chyba nawet nie jest niezbędne, ale ja tak widziałam i skopiowałam, więc nie wiem czemu to służy. 

# This is the end
Dobra, ide jesc leczo. Mam nadzieję, że się przyda, ale nawet jeśli nie to wiem jak pisać README, yay!
