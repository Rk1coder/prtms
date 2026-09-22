export default function SummaryCards({ platforms }) {
  const cards = [
    ['Total platforms', platforms.length],
    ['Ready', platforms.filter((p) => p.status === 'READY').length],
    ['Degraded', platforms.filter((p) => p.status === 'DEGRADED').length],
    ['Not ready', platforms.filter((p) => p.status === 'NOT_READY').length],
  ];

  return <section className="summary-grid" aria-label="Platform summary">
    {cards.map(([label, count]) => <div className="summary-card" key={label}>
      <span>{label}</span><strong>{count}</strong>
    </div>)}
  </section>;
}
