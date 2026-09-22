import StatusBadge from './StatusBadge.jsx';

export default function PlatformTable({ platforms, selectedPlatform, onSelect, loading, busy }) {
  return <section className="panel">
    <div className="section-heading"><div><p className="eyebrow">OVERVIEW</p><h2>Platform Registry</h2></div><span className="count-label">{platforms.length} platforms</span></div>
    {loading && <p role="status" className="muted">Loading...</p>}
    {!loading && platforms.length === 0 ? <div className="empty-state"><strong>No platforms registered.</strong><p>Add your first platform using the form above.</p></div> : platforms.length > 0 && <div className="table-scroll">
      <table><thead><tr><th>Platform Code</th><th>Name</th><th>Type</th><th>Status</th><th>Action</th></tr></thead>
        <tbody>{platforms.map((platform) => <tr key={platform.id} className={selectedPlatform?.id === platform.id ? 'selected-row' : ''}>
          <td className="code">{platform.platformCode}</td><td>{platform.name}</td><td>{platform.type}</td><td><StatusBadge status={platform.status} /></td>
          <td><button className="secondary small" disabled={busy} aria-pressed={selectedPlatform?.id === platform.id} aria-label={`View ${platform.platformCode}`} onClick={() => onSelect(platform)}>{selectedPlatform?.id === platform.id ? 'Selected' : 'View'}</button></td>
        </tr>)}</tbody>
      </table>
    </div>}
  </section>;
}
