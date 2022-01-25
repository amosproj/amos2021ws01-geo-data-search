import Leaflet from 'leaflet';

const regularIconSize = {
  width: 20,
  height: 38,
};

const largeIconSize = {
  width: 24,
  height: 46,
};

const startIconSize = {
  width: 14,
  height: 14,
}

const BaseRegularMarkerIcon = Leaflet.Icon.extend({
  options: {
    iconSize: [regularIconSize.width, regularIconSize.height],
    iconAnchor: [regularIconSize.width / 2, regularIconSize.height],
    popupAnchor: [0, -regularIconSize.height],
  },
});

const BaseLargeMarkerIcon = Leaflet.Icon.extend({
  options: {
    iconSize: [largeIconSize.width, largeIconSize.height],
    iconAnchor: [largeIconSize.width / 2, largeIconSize.height],
    popupAnchor: [0, -largeIconSize.height],
  },
});

export const generalMarker = {
  // @ts-ignore
  regular: new BaseRegularMarkerIcon({
    iconUrl: '/images/marker-regular.svg',
  }) as Leaflet.Icon<Leaflet.IconOptions>,
  // @ts-ignore
  large: new BaseLargeMarkerIcon({
    iconUrl: '/images/marker-regular.svg',
  }) as Leaflet.Icon<Leaflet.IconOptions>,
};

export const chargingStationMarker = {
  // @ts-ignore
  regular: new BaseRegularMarkerIcon({
    iconUrl: '/images/marker-charging.svg',
  }) as Leaflet.Icon<Leaflet.IconOptions>,
  // @ts-ignore
  large: new BaseLargeMarkerIcon({
    iconUrl: '/images/marker-charging.svg',
  }) as Leaflet.Icon<Leaflet.IconOptions>,
};

export const markersByType: {
  [key: string]: {
    regular: Leaflet.Icon<Leaflet.IconOptions>;
    large: Leaflet.Icon<Leaflet.IconOptions>;
  };
} = {
  chargingStation: chargingStationMarker,
};

export const startMarkerIcon = new Leaflet.Icon({
  iconUrl: '/images/marker-route.svg',
  iconSize: [startIconSize.height, startIconSize.width],
  iconAnchor: [startIconSize.width / 2, startIconSize.height / 2],
  popupAnchor: [0, -startIconSize.height],
});
