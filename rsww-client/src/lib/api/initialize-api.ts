const initialize = async () => {
  const response = await fetch(`${process.env.API_GATEWAY_ADDRESS}/initialize`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  if (!response.ok) {
    throw new Error(`API request failed with status ${response.status}`);
  }

  const data = await response.json();
  return data;
};

export { initialize };