Resumen:

En el MainActivity el metodo getServerData es el encargado de iniciar el servicio para hacer las llamadas al servidor con retrofit

Ese service se encuentra definido dentro del package rest TaxiListApiService, tambien tenemos definido un adapter BookingListApiAdapter que

nos permite configurar y tener accesible a los metodos del api service.

En el metodo success se recibe la respuesta del servidor a atraves de un jsonObject y se setea una variable con ese resultadto para usarlo luego

ademas se agrega el resultado a un TextField para mostrarlo en pantalla

Tambien en ese metodo se obtiene la posicion del dispositivo mediante un locationManager y se llama al metodo onLocationChanged

En el metodo onLocationChanged creo un objeto de tipo LatLng con la posicion actual, luego itero sobre el array de bookings del json que viene

del servidor para crear objetos de tipo LatLng por cada uno e insertarlos en una lista de coordenadas

Tambien se hace una llamada al metodo getCentroid para obtener el centroide de una lista de coordenadas

y por ultimo se muestra la posicion actual del dispositivo en google maps



