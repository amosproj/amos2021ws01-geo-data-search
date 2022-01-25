import React, { useEffect, useMemo, useRef } from 'react';
import {
  MapContainer,
  Marker,
  TileLayer,
  Tooltip,
  ZoomControl,
  useMapEvents,
  useMap,
  Polyline,
} from 'react-leaflet';
import Leaflet, { LatLngExpression } from 'leaflet';
import 'leaflet/dist/leaflet.css';
import { useAtom } from 'jotai';
import { currentSearchResultAtom, searchResultsAtom } from '@lib/store';
import { generalMarker, markersByType, startMarkerIcon } from './markerIcon';
// @ts-ignore
import FlexPolyline from '@liberty-rider/flexpolyline';
import { SearchResult } from '@lib/types/search';

// Berlin center
const position: LatLngExpression = [52.520008, 13.404954];

const getMarkerPosition = ({ lat, lon }: any) => {
  return {
    lat: parseFloat(lat),
    lng: parseFloat(lon),
  };
};

const MapInner = () => {
  const [searchResults] = useAtom(searchResultsAtom);
  // Filter out results without valid lat / lon
  const results = useMemo(
    () => (searchResults ? searchResults.filter((i) => i.lat && i.lon) : searchResults),
    [searchResults]
  );

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
    // Runs every time the search results are updated
    if (results) {
      // Re-calculate the new bounds with the new results
      const bounds = Leaflet.latLngBounds(results.map(getMarkerPosition));

      // Re-fit the map to the new bounds
      map.fitBounds(bounds, {
        padding: [50, 50],
      });
      // console.log('Fitting map to the updated bounds', bounds);

      // Update the marker label visibility based on the new map bounds
      updateLabelsVisibility();
    }
  }, [results]);

  const routePolylines = useMemo(() => {
    if (!results) return null;

    const routeResults = results.filter((i) => !!i.polyline);

    if (routeResults.length > 0) {
      return routeResults.map(
        (i) => FlexPolyline.decode(i.polyline)?.polyline as [number, number][]
      );
    }

    return null;
  }, [results]);

  const getMarkerIcon = (result: SearchResult) => {
    if (isRoute && result.type === 'Start') {
      return startMarkerIcon;
    }

    const markerIconSet = markersByType[result.type] || generalMarker;
    const markerIcon =
      currentSearchResult?.id === result.id ? markerIconSet.large : markerIconSet.regular;

    return markerIcon;
  };

  const isRoute = routePolylines !== null;

  return (
    <>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomright" />
      {results && (
        <>
          {isRoute &&
            routePolylines.map((positions, index) => (
              <Polyline
                key={index}
                pathOptions={{
                  color: '#018FCC',
                }}
                positions={positions}
              />
            ))}

          {results.map((result, index) => {
            const markerIcon = getMarkerIcon(result);

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
      )}
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
