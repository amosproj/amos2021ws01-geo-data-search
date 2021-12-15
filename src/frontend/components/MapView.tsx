import React, { useEffect, useRef } from 'react';
import { MapContainer, Marker, TileLayer, ZoomControl } from 'react-leaflet';
import Leaflet, { LatLngExpression } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useAtom } from 'jotai';
import { currentSearchResultAtom, searchResultsAtom } from '@lib/store';
import { largeMarkerIcon, regularMarkerIcon } from './markerIcon';

// Berlin center
const position: LatLngExpression = [52.520008, 13.404954];

const getMarkerPosition = ({ lat, lon }: any) => {
  return {
    lat: parseFloat(lat),
    lng: parseFloat(lon),
  };
};

const MapView = () => {
  const [results] = useAtom(searchResultsAtom);
  const [currentSearchResult, setCurrentSearchResult] = useAtom(currentSearchResultAtom);
  const mapRef = useRef<Leaflet.Map | null>(null);

  useEffect(() => {
    if (mapRef.current && currentSearchResult) {
      mapRef.current.setView(getMarkerPosition(currentSearchResult));
    }
  }, [currentSearchResult]);

  useEffect(() => {
    if (results && mapRef.current) {
      // Calculate the new bounds with the new results
      const bounds = Leaflet.latLngBounds(results.map(getMarkerPosition));

      // Re-fit the map to the new bounds
      mapRef.current.fitBounds(bounds, {
        padding: [50, 50],
      });
      console.log('Fitting map to the updated bounds', bounds);
    }
  }, [results]);

  // Save the map instance for later usage
  const onMapCreated = (map: Leaflet.Map) => {
    mapRef.current = map;
  };

  return (
    <MapContainer
      center={position}
      zoom={13}
      className="h-full"
      zoomControl={false}
      whenCreated={onMapCreated}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomright" />
      {results &&
        results.map((result) => {
          const markerIcon =
            currentSearchResult?.id === result.id ? largeMarkerIcon : regularMarkerIcon;

          return (
            <Marker
              key={result.id}
              icon={markerIcon}
              position={getMarkerPosition(result)}
              eventHandlers={{
                click: () => {
                  setCurrentSearchResult(result);
                },
              }}
            />
          );
        })}
    </MapContainer>
  );
};

export default MapView;
