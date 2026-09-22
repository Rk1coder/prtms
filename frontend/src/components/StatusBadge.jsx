export default function StatusBadge({ status = 'UNKNOWN' }) {
  return <span className={`status-badge status-${status.toLowerCase()}`}>
    <span aria-hidden="true" className="status-dot" />{status.replace('_', ' ')}
  </span>;
}
