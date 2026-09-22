import { useState } from 'react';

export default function PlatformForm({ onCreate, busy }) {
  const [code, setCode] = useState('');
  const [name, setName] = useState('');
  const [type, setType] = useState('UAV');
  const [message, setMessage] = useState(null);

  async function handleSubmit(event) {
    event.preventDefault();
    setMessage(null);
    try {
      await onCreate({ platformCode: code.trim(), name: name.trim(), type });
      setCode(''); setName(''); setType('UAV');
      setMessage({ kind: 'success', text: 'Platform added successfully.' });
    } catch (error) {
      setMessage({ kind: 'error', text: error.message });
    }
  }

  return <section className="panel">
    <div className="section-heading"><div><p className="eyebrow">REGISTER</p><h2>Add Platform</h2></div></div>
    <p className="muted">Register a platform to start sending synthetic telemetry.</p>
    <form onSubmit={handleSubmit}>
      <fieldset disabled={busy} className="platform-form">
        <label>Platform Code<input required maxLength={255} value={code} onChange={(e) => setCode(e.target.value)} placeholder="UAV-001" /></label>
        <label>Platform Name<input required maxLength={255} value={name} onChange={(e) => setName(e.target.value)} placeholder="Demo UAV" /></label>
        <label>Platform Type<select value={type} onChange={(e) => setType(e.target.value)}>
          <option>UAV</option><option>UGV</option><option>SENSOR</option>
        </select></label>
        <button type="submit">{busy ? 'Adding...' : 'Add Platform'}</button>
      </fieldset>
    </form>
    {message && <div role={message.kind === 'error' ? 'alert' : 'status'} className={`message ${message.kind}`}>{message.text}</div>}
  </section>;
}
