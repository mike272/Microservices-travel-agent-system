const initialize = async () => {

  const response = await fetch(
    `${process.env.NEXT_PUBLIC_API_GATEWAY_ADDRESS}/v1/hotels/initialize`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error(`API request failed with status ${response.status}`);
  }

  const data = await response.json();
  return data;
};

export { initialize };
