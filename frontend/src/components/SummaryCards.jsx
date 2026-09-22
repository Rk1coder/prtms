export default function SummaryCards({ platforms }) {
  const cards = [
    ['TOTAL PLATFORMS', platforms.length, 'total', 'Registered in this session'],
    ['READY', platforms.filter((p) => p.status === 'READY').length, 'ready', 'All metrics within demo limits'],
    ['DEGRADED', platforms.filter((p) => p.status === 'DEGRADED').length, 'degraded', 'At least one warning metric'],
    ['NOT READY', platforms.filter((p) => p.status === 'NOT_READY').length, 'not-ready', 'At least one critical metric'],
  ];
  return <section className="summary-grid" aria-label="Platform summary">
    {cards.map(([label, count, color, description]) => <article className={`summary-card ${color}`} key={label}>
      <h2>{label}</h2><strong>{count}</strong><p>{description}</p>
    </article>)}
  </section>;
}
