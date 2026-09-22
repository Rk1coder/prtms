const API_BASE_URL = 'http://localhost:8080/api';

async function request(path, options = {}) {
  let response;
  try {
    response = await fetch(`${API_BASE_URL}${path}`, options);
  } catch {
    throw new Error('Backend connection error. Check that the backend is running on port 8080.');
  }
  const data = await response.json();
  if (!response.ok) {
    const details = data.errors
      ? Object.entries(data.errors).map(([field, message]) => `${field}: ${message}`).join('; ')
      : data.message;
    throw new Error(details || `Request failed (${response.status}).`);
  }
  return data;
}

function post(path, data) {
  return request(path, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
}

export const getPlatforms = () => request('/platforms');
export const createPlatform = (platform) => post('/platforms', platform);
export const sendTelemetry = (telemetry) => post('/telemetry', telemetry);
export const getTelemetryHistory = (code) => request(`/platforms/${encodeURIComponent(code)}/telemetry`);
export const getReadiness = (code) => request(`/platforms/${encodeURIComponent(code)}/readiness`);
