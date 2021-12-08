type ApiClientConfig = {
  method: "POST" | "GET" | "PUT" | "DELETE";
  headers?: {
    [key: string]: string;
  };
  body?: URLSearchParams;
};

export default function osmApiClient(
  endpoint: string,
  { body }: { [key: string]: any } = {}
) {
  const config: ApiClientConfig = {
    method: body ? "POST" : "GET",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
  };

  if (body) {
    config.body = new URLSearchParams(body);
  }

  return fetch(
    `/api${endpoint}`,
    config
  );
}
