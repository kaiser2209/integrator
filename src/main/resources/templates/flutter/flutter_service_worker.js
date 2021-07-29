'use strict';
const MANIFEST = 'flutter-app-manifest';
const TEMP = 'flutter-temp-cache';
const CACHE_NAME = 'flutter-app-cache';
const RESOURCES = {
  "assets/AssetManifest.json": "0b1c9331f301d3bd3f1c0ce98d583a16",
"assets/FontManifest.json": "bb92cb5cb486cbe781146e91090321f1",
"assets/fonts/MaterialIcons-Regular.otf": "1288c9e28052e028aba623321f7826ac",
"assets/icons/dialog_error.png": "e3454b10175904bf8745ee21ec592391",
"assets/icons/dialog_success.png": "9cf9596f018bb04760464b84cf2a49f1",
"assets/icons/dialog_warning.png": "0c19e2b61e08ce9edd1059bbafa31d26",
"assets/images/icon-mobi-wtfundo.png": "6b45aa6d2367fa70c82b9225fc81781e",
"assets/images/iconMobi.png": "242aec17973aa71f90f2f12627c26a54",
"assets/images/logo.png": "b8bf91d73813bf155bd54e46dad1fad8",
"assets/images/logoMobi.png": "f2b55d05e6aae86f3975582fec81d27e",
"assets/images/textura.png": "c6dc2c16c90287cebd8e641cab2e319d",
"assets/NOTICES": "ee3912f49e7b8d3b9e899a2fe52ffd33",
"assets/packages/cupertino_icons/assets/CupertinoIcons.ttf": "b14fcf3ee94e3ace300b192e9e7c8c5d",
"assets/packages/mobiplus_flutter_ui/assets/fonts/OpenSans-Bold.ttf": "50145685042b4df07a1fd19957275b81",
"assets/packages/mobiplus_flutter_ui/assets/fonts/OpenSans-Regular.ttf": "629a55a7e793da068dc580d184cc0e31",
"assets/packages/mobiplus_flutter_ui/assets/images/icon-mobi-wtfundo.png": "d4e5e0e1771f267ba24c7e6ffdcdad3e",
"assets/packages/mobiplus_flutter_ui/assets/images/icon_back.png": "a54bfd57010f3a150ee9f67cede0e626",
"assets/packages/mobiplus_flutter_ui/assets/images/icon_circle.png": "2d99a48d9f15b94a318d16b08391a7f6",
"assets/packages/mobiplus_flutter_ui/assets/images/icon_circle_filled.png": "7f6602778f945f3ae47c3a69cb78e083",
"assets/packages/mobiplus_flutter_ui/assets/images/icon_filtro.png": "19963695a55b9bdd79f2cf9df3fa8585",
"assets/packages/mobiplus_flutter_ui/assets/images/Login-Mobile.png": "50f1cdbce9b9325b751c0cb51bb0d95f",
"assets/packages/mobiplus_flutter_ui/assets/images/Login-Web.png": "4af61d7fec6f54e71e20ede67b58d2bc",
"assets/packages/mobiplus_flutter_ui/assets/images/logo-branca.png": "6b45aa6d2367fa70c82b9225fc81781e",
"assets/packages/mobiplus_flutter_ui/assets/images/logo.png": "b7d14affd0ac6f62bb4f35cee2a86209",
"assets/packages/mobiplus_flutter_ui/assets/videos/loading_mobi.gif": "a87cff778b95bf24ac1947af36c74bec",
"assets/web/assets/icons/dialog_error.png": "e3454b10175904bf8745ee21ec592391",
"assets/web/assets/icons/dialog_success.png": "9cf9596f018bb04760464b84cf2a49f1",
"assets/web/assets/icons/dialog_warning.png": "0c19e2b61e08ce9edd1059bbafa31d26",
"assets/web/assets/images/icon-mobi-wtfundo.png": "6b45aa6d2367fa70c82b9225fc81781e",
"assets/web/assets/images/iconMobi.png": "242aec17973aa71f90f2f12627c26a54",
"assets/web/assets/images/logo.png": "b8bf91d73813bf155bd54e46dad1fad8",
"assets/web/assets/images/logoMobi.png": "f2b55d05e6aae86f3975582fec81d27e",
"assets/web/assets/images/textura.png": "c6dc2c16c90287cebd8e641cab2e319d",
"favicon.png": "5dcef449791fa27946b3d35ad8803796",
"icons/Icon-192.png": "ac9a721a12bbc803b44f645561ecb1e1",
"icons/Icon-512.png": "96e752610906ba2a93c65f8abe1645f1",
"index.html": "a27d31d1627bf86128fa1318a332c7a9",
"/": "a27d31d1627bf86128fa1318a332c7a9",
"main.dart.js": "eb285a329442c2410c81cbd1df43b38f",
"manifest.json": "a14ec3402d9c848c1ae2c8db6f5de921",
"version.json": "ec683e320085e4d1b58ef6007c02bbf7"
};

// The application shell files that are downloaded before a service worker can
// start.
const CORE = [
  "/",
"main.dart.js",
"index.html",
"assets/NOTICES",
"assets/AssetManifest.json",
"assets/FontManifest.json"];
// During install, the TEMP cache is populated with the application shell files.
self.addEventListener("install", (event) => {
  self.skipWaiting();
  return event.waitUntil(
    caches.open(TEMP).then((cache) => {
      return cache.addAll(
        CORE.map((value) => new Request(value, {'cache': 'reload'})));
    })
  );
});

// During activate, the cache is populated with the temp files downloaded in
// install. If this service worker is upgrading from one with a saved
// MANIFEST, then use this to retain unchanged resource files.
self.addEventListener("activate", function(event) {
  return event.waitUntil(async function() {
    try {
      var contentCache = await caches.open(CACHE_NAME);
      var tempCache = await caches.open(TEMP);
      var manifestCache = await caches.open(MANIFEST);
      var manifest = await manifestCache.match('manifest');
      // When there is no prior manifest, clear the entire cache.
      if (!manifest) {
        await caches.delete(CACHE_NAME);
        contentCache = await caches.open(CACHE_NAME);
        for (var request of await tempCache.keys()) {
          var response = await tempCache.match(request);
          await contentCache.put(request, response);
        }
        await caches.delete(TEMP);
        // Save the manifest to make future upgrades efficient.
        await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
        return;
      }
      var oldManifest = await manifest.json();
      var origin = self.location.origin;
      for (var request of await contentCache.keys()) {
        var key = request.url.substring(origin.length + 1);
        if (key == "") {
          key = "/";
        }
        // If a resource from the old manifest is not in the new cache, or if
        // the MD5 sum has changed, delete it. Otherwise the resource is left
        // in the cache and can be reused by the new service worker.
        if (!RESOURCES[key] || RESOURCES[key] != oldManifest[key]) {
          await contentCache.delete(request);
        }
      }
      // Populate the cache with the app shell TEMP files, potentially overwriting
      // cache files preserved above.
      for (var request of await tempCache.keys()) {
        var response = await tempCache.match(request);
        await contentCache.put(request, response);
      }
      await caches.delete(TEMP);
      // Save the manifest to make future upgrades efficient.
      await manifestCache.put('manifest', new Response(JSON.stringify(RESOURCES)));
      return;
    } catch (err) {
      // On an unhandled exception the state of the cache cannot be guaranteed.
      console.error('Failed to upgrade service worker: ' + err);
      await caches.delete(CACHE_NAME);
      await caches.delete(TEMP);
      await caches.delete(MANIFEST);
    }
  }());
});

// The fetch handler redirects requests for RESOURCE files to the service
// worker cache.
self.addEventListener("fetch", (event) => {
  if (event.request.method !== 'GET') {
    return;
  }
  var origin = self.location.origin;
  var key = event.request.url.substring(origin.length + 1);
  // Redirect URLs to the index.html
  if (key.indexOf('?v=') != -1) {
    key = key.split('?v=')[0];
  }
  if (event.request.url == origin || event.request.url.startsWith(origin + '/#') || key == '') {
    key = '/';
  }
  // If the URL is not the RESOURCE list then return to signal that the
  // browser should take over.
  if (!RESOURCES[key]) {
    return;
  }
  // If the URL is the index.html, perform an online-first request.
  if (key == '/') {
    return onlineFirst(event);
  }
  event.respondWith(caches.open(CACHE_NAME)
    .then((cache) =>  {
      return cache.match(event.request).then((response) => {
        // Either respond with the cached resource, or perform a fetch and
        // lazily populate the cache.
        return response || fetch(event.request).then((response) => {
          cache.put(event.request, response.clone());
          return response;
        });
      })
    })
  );
});

self.addEventListener('message', (event) => {
  // SkipWaiting can be used to immediately activate a waiting service worker.
  // This will also require a page refresh triggered by the main worker.
  if (event.data === 'skipWaiting') {
    self.skipWaiting();
    return;
  }
  if (event.data === 'downloadOffline') {
    downloadOffline();
    return;
  }
});

// Download offline will check the RESOURCES for all files not in the cache
// and populate them.
async function downloadOffline() {
  var resources = [];
  var contentCache = await caches.open(CACHE_NAME);
  var currentContent = {};
  for (var request of await contentCache.keys()) {
    var key = request.url.substring(origin.length + 1);
    if (key == "") {
      key = "/";
    }
    currentContent[key] = true;
  }
  for (var resourceKey of Object.keys(RESOURCES)) {
    if (!currentContent[resourceKey]) {
      resources.push(resourceKey);
    }
  }
  return contentCache.addAll(resources);
}

// Attempt to download the resource online before falling back to
// the offline cache.
function onlineFirst(event) {
  return event.respondWith(
    fetch(event.request).then((response) => {
      return caches.open(CACHE_NAME).then((cache) => {
        cache.put(event.request, response.clone());
        return response;
      });
    }).catch((error) => {
      return caches.open(CACHE_NAME).then((cache) => {
        return cache.match(event.request).then((response) => {
          if (response != null) {
            return response;
          }
          throw error;
        });
      });
    })
  );
}
