import Leaflet from 'leaflet';

export const regularMarkerIcon = Leaflet.icon({
  iconUrl: '/images/map-marker.svg',
  iconSize: [28, 42],
  iconAnchor: [14, 42],
  popupAnchor: [0, -42],
});

export const largeMarkerIcon = Leaflet.icon({
  iconUrl: '/images/map-marker.svg',
  iconSize: [36, 54],
  iconAnchor: [18, 54],
  popupAnchor: [0, -54],
});
