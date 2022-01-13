import React, { useEffect, useRef } from 'react';
import {
  MapContainer,
  Marker,
  TileLayer,
  Tooltip,
  ZoomControl,
  useMapEvents,
  useMap,
} from 'react-leaflet';
import Leaflet, { LatLngExpression } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useAtom } from 'jotai';
import { currentSearchResultAtom, searchResultsAtom } from '@lib/store';
import { generalMarker, markersByType } from './markerIcon';

// Berlin center
const position: LatLngExpression = [52.520008, 13.404954];

const getMarkerPosition = ({ lat, lon }: any) => {
  return {
    lat: parseFloat(lat),
    lng: parseFloat(lon),
  };
};

const MapInner = () => {
  const [results] = useAtom(searchResultsAtom);
  const [currentSearchResult, setCurrentSearchResult] = useAtom(currentSearchResultAtom);
  const map = useMap();
  const labelsRef = useRef<any[]>([]);

  const updateLabelsVisibility = () => {
    let visibleIdx: number[] = [];

    for (let i = 0; i < labelsRef.current.length; i++) {
      const label = labelsRef.current[i];
      if (!label) continue;
      const rect1 = label._container.getBoundingClientRect();

      let colliding = false;

      for (let vIndex of visibleIdx) {
        const v = labelsRef.current[vIndex];
        const rect2 = v._container.getBoundingClientRect();

        colliding = !(
          rect1.right < rect2.left ||
          rect1.left > rect2.right ||
          rect1.bottom < rect2.top ||
          rect1.top > rect2.bottom
        );

        if (colliding) {
          break;
        }
      }

      if (!colliding) {
        visibleIdx.push(i);
      }
    }

    for (let i = 0; i < labelsRef.current.length; i++) {
      const label = labelsRef.current[i];
      if (label) {
        label._container.style.opacity = visibleIdx.includes(i) ? 1 : 0;
      }
    }
  };

  useMapEvents({
    zoomend: updateLabelsVisibility,
  });

  useEffect(() => {
    if (currentSearchResult) {
      map.setView(getMarkerPosition(currentSearchResult));
    }
  }, [currentSearchResult]);

  useEffect(() => {
    if (results) {
      // Calculate the new bounds with the new results
      const bounds = Leaflet.latLngBounds(results.map(getMarkerPosition));

      // Re-fit the map to the new bounds
      map.fitBounds(bounds, {
        padding: [50, 50],
      });
      console.log('Fitting map to the updated bounds', bounds);

      updateLabelsVisibility();
    }
  }, [results]);

  return (
    <>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomright" />
      {results &&
        results.map((result, index) => {
          const markerIconSet = markersByType[result.type] || generalMarker;
          const markerIcon =
            currentSearchResult?.id === result.id ? markerIconSet.large : markerIconSet.regular;

          const id = `${result.id}${index}`;
          // @ts-ignore
          const height = markerIcon.options.iconSize?.[0] ?? 0;

          const onSearchResultClick = () => {
            setCurrentSearchResult(result);
          };

          return (
            <Marker
              key={id}
              icon={markerIcon}
              position={getMarkerPosition(result)}
              eventHandlers={{
                click: onSearchResultClick,
              }}
            >
              <Tooltip
                ref={(el) => {
                  labelsRef.current[index] = el;
                }}
                direction="right"
                offset={[0, (-height * 2) / 3]}
                opacity={0}
                permanent
              >
                <span>{result.name}</span>
              </Tooltip>
            </Marker>
          );
        })}
    </>
  );
};

const MapView = () => {
  return (
    <MapContainer center={position} zoom={13} className="h-full" zoomControl={false}>
      <MapInner />
    </MapContainer>
  );
};

export default MapView;
