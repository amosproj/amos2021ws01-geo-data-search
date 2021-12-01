import React from 'react';
import { MapContainer, TileLayer, ZoomControl } from 'react-leaflet';
import { LatLngExpression } from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Berlin center
const position: LatLngExpression = [52.520008, 13.404954];

const MapView = () => {
  return (
    <MapContainer className="h-full" center={position} zoom={13} zoomControl={false}>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomright" />
    </MapContainer>
  );
};

export default MapView;
