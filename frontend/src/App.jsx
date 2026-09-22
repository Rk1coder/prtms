import { useEffect, useState } from 'react';
import SummaryCards from './components/SummaryCards.jsx';
import PlatformForm from './components/PlatformForm.jsx';
import PlatformTable from './components/PlatformTable.jsx';
import TelemetryPanel from './components/TelemetryPanel.jsx';
import * as api from './services/api.js';

export default function App() {
  const [platforms, setPlatforms] = useState([]);
  const [selectedPlatform, setSelectedPlatform] = useState(null);
  const [telemetryHistory, setTelemetryHistory] = useState([]);
  const [readiness, setReadiness] = useState(null);
  const [loading, setLoading] = useState(true);
  const [detailLoading, setDetailLoading] = useState(false);
  const [savingPlatform, setSavingPlatform] = useState(false);
  const [sendingTelemetry, setSendingTelemetry] = useState(false);
  const [error, setError] = useState('');
  const [detailError, setDetailError] = useState('');

  async function refreshPlatforms() {
    setLoading(true); setError('');
    try {
      const data = await api.getPlatforms();
      setPlatforms(data);
      setSelectedPlatform((current) => current ? data.find((p) => p.id === current.id) || null : null);
    } catch (failure) {
      setError(failure.message);
    } finally { setLoading(false); }
  }

  useEffect(() => { refreshPlatforms(); }, []);

  async function selectPlatform(platform) {
    setSelectedPlatform(platform); setReadiness(null); setTelemetryHistory([]);
    setDetailLoading(true); setDetailError('');
    try {
      const [nextReadiness, history] = await Promise.all([
        api.getReadiness(platform.platformCode), api.getTelemetryHistory(platform.platformCode),
      ]);
      setReadiness(nextReadiness); setTelemetryHistory(history);
    } catch (failure) { setDetailError(failure.message); }
    finally { setDetailLoading(false); }
  }

  async function createPlatform(platform) {
    setSavingPlatform(true);
    try {
      await api.createPlatform(platform);
      await refreshPlatforms();
    } finally { setSavingPlatform(false); }
  }

  async function sendTelemetry(telemetry) {
    setSendingTelemetry(true);
    try {
      const saved = await api.sendTelemetry(telemetry);
      await refreshPlatforms();
      await selectPlatform({ ...selectedPlatform, status: saved.status });
    } finally { setSendingTelemetry(false); }
  }

  return <main>
    <header className="page-header"><div className="brand-mark" aria-hidden="true">P</div><div><p className="eyebrow">PRTMS / PLATFORM MONITOR</p><h1>Platform Readiness &amp;<br className="desktop-break" /> Telemetry Monitoring System</h1><p className="subtitle">Synthetic Platform Health Monitoring POC</p></div><span className="demo-tag">SYNTHETIC DATA ONLY</span></header>
    <SummaryCards platforms={platforms} />
    {error && <div className="message error" role="alert">{error} <button className="secondary small" disabled={loading} onClick={refreshPlatforms}>Retry</button></div>}
    <PlatformForm onCreate={createPlatform} busy={savingPlatform} />
    <PlatformTable platforms={platforms} selectedPlatform={selectedPlatform} onSelect={selectPlatform} loading={loading} busy={detailLoading || sendingTelemetry} />
    {selectedPlatform ? <TelemetryPanel key={selectedPlatform.id} platform={selectedPlatform} readiness={readiness} history={telemetryHistory} loading={detailLoading} busy={sendingTelemetry} error={detailError} onSend={sendTelemetry} /> : <section className="selection-hint"><span aria-hidden="true">↳</span> Select a platform to view readiness and send telemetry.</section>}
    <footer><span>PRTMS · Learning POC</span><span>Synthetic metrics. Demo thresholds. In-memory storage.</span></footer>
  </main>;
}
