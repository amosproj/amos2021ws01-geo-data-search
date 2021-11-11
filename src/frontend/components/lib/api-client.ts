type ApiClientConfig = {
  method: "POST" | "GET" | "PUT" | "DELETE";
  headers?: {
    [key: string]: string;
  };
  body?: string;
};

export default function apiClient(
  endpoint: string,
  { body }: { [key: string]: any } = {}
) {
  const config: ApiClientConfig = {
    method: body ? "POST" : "GET",
    headers: {
      "Content-Type": "application/json",
    },
  };

  if (body) {
    config.body = JSON.stringify(body);
  }

  return fetch(`/api${endpoint}`, config);
}
