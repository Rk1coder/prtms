import { useEffect, useState } from 'react';
import SummaryCards from './components/SummaryCards.jsx';
import PlatformForm from './components/PlatformForm.jsx';
import PlatformTable from './components/PlatformTable.jsx';
import * as api from './services/api.js';

export default function App() {
  const [platforms, setPlatforms] = useState([]);
  const [selectedPlatform, setSelectedPlatform] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  async function refreshPlatforms() {
    setLoading(true);
    setError('');
    try { setPlatforms(await api.getPlatforms()); }
    catch (failure) { setError(failure.message); }
    finally { setLoading(false); }
  }
  useEffect(() => { refreshPlatforms(); }, []);

  async function createPlatform(platform) {
    setSaving(true);
    try { await api.createPlatform(platform); await refreshPlatforms(); }
    finally { setSaving(false); }
  }

  return <main>
    <h1>Platform Readiness &amp; Telemetry Monitoring System</h1>
    <SummaryCards platforms={platforms} />
    {error && <p role="alert">{error}</p>}
    <PlatformForm onCreate={createPlatform} busy={saving} />
    <PlatformTable platforms={platforms} selectedPlatform={selectedPlatform}
      onSelect={setSelectedPlatform} loading={loading} busy={saving} />
  </main>;
}
