type ApiClientConfig = {
  method: 'POST' | 'GET' | 'PUT' | 'DELETE';
  headers?: {
    [key: string]: string;
  };
  body?: URLSearchParams;
  signal?: AbortSignal | null;
};

export type CancelablePromise<T> = Promise<T> & {
  cancel?: () => void;
};

export default function apiClient(endpoint: string, { body }: { [key: string]: any } = {}) {
  const controller = new AbortController();

  const config: ApiClientConfig = {
    method: body ? 'POST' : 'GET',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    signal: controller.signal,
  };

  if (body) {
    config.body = new URLSearchParams(body);
  }

  const promise: CancelablePromise<Response> = fetch(`/api${endpoint}`, config);

  // Add a way to cancel the API request
  promise.cancel = () => controller.abort();

  return promise;
}
