import { useState } from 'react';
import StatusBadge from './StatusBadge.jsx';

function formatTime(value) {
  return value ? new Date(value).toLocaleString(undefined, { dateStyle: 'medium', timeStyle: 'medium' }) : 'No telemetry yet';
}

export default function TelemetryPanel({ platform, readiness, history, loading, busy, error, onSend }) {
  const [battery, setBattery] = useState('');
  const [temperature, setTemperature] = useState('');
  const [link, setLink] = useState('');
  const [message, setMessage] = useState(null);

  async function handleSubmit(event) {
    event.preventDefault();
    setMessage(null);
    try {
      await onSend({ platformCode: platform.platformCode, batteryLevel: Number(battery), temperature: Number(temperature), linkQuality: Number(link) });
      setMessage({ kind: 'success', text: 'Telemetry saved. Platform health updated.' });
    } catch (failure) {
      setMessage({ kind: 'error', text: failure.message });
    }
  }

  return <section className="panel selected-panel" aria-label="Selected Platform">
    <div className="section-heading"><div><p className="eyebrow">SELECTED PLATFORM</p><h2 className="code">{platform.platformCode}</h2><p className="muted platform-meta">{platform.name} · {platform.type}</p></div><div><span className="field-caption">Current Status</span><StatusBadge status={platform.status} /></div></div>
    <div className="telemetry-grid">
      <div><h3>Send Telemetry</h3><p className="muted">Enter a synthetic reading to assess platform health.</p>
        <form onSubmit={handleSubmit}><fieldset disabled={busy || loading} className="telemetry-form">
          <label>Battery %<input type="number" min="0" max="100" step="1" required value={battery} onChange={(e) => setBattery(e.target.value)} placeholder="80" /></label>
          <label>Temperature °C<input type="number" step="any" required value={temperature} onChange={(e) => setTemperature(e.target.value)} placeholder="55" /></label>
          <label>Link Quality %<input type="number" min="0" max="100" step="1" required value={link} onChange={(e) => setLink(e.target.value)} placeholder="90" /></label>
          <button type="submit">{busy ? 'Sending...' : 'Send Telemetry'}</button>
        </fieldset></form>
        {message && <div className={`message ${message.kind}`} role={message.kind === 'error' ? 'alert' : 'status'}>{message.text}</div>}
      </div>
      <aside className="readiness-card"><p className="eyebrow">CURRENT READINESS</p><h3 className="code">{platform.platformCode}</h3>
        {loading ? <p role="status">Loading...</p> : readiness ? <><StatusBadge status={readiness.status} /><span className="field-caption">Latest Telemetry</span><time>{formatTime(readiness.latestTelemetryTime)}</time></> : <p className="muted">Readiness unavailable.</p>}
      </aside>
    </div>
    {error && <div className="message error" role="alert">{error}</div>}
    <div className="history-heading"><h3>Telemetry History</h3><span className="count-label">Latest 10 readings · newest first</span></div>
    {loading ? <p role="status">Loading...</p> : error ? <p className="muted">History unavailable. Select the platform again to retry.</p> : history.length === 0 ? <div className="empty-state">No telemetry available.</div> : <div className="table-scroll"><table>
      <thead><tr><th>Time</th><th>Battery</th><th>Temperature</th><th>Link Quality</th><th>Status</th></tr></thead>
      <tbody>{history.map((entry) => <tr key={entry.id}><td><time>{formatTime(entry.timestamp)}</time></td><td>{entry.batteryLevel}%</td><td>{entry.temperature}°C</td><td>{entry.linkQuality}%</td><td><StatusBadge status={entry.status} /></td></tr>)}</tbody>
    </table></div>}
    <p className="footnote">Demo thresholds only. Readiness reflects the latest submitted reading; no automatic freshness check.</p>
  </section>;
}
