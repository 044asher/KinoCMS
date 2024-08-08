document.addEventListener('DOMContentLoaded', function () {
    const ctx = document.getElementById('genderChart').getContext('2d');
    new Chart(ctx, {
        type: 'pie',
        data: {
            labels: ['Male', 'Female', 'Non-Binary', 'Other', 'Prefer Not to Say'],
            datasets: [{
                label: 'Gender Distribution',
                data: [
                    [[${maleCount}]],
                    [[${femaleCount}]],
                    [[${nonBinaryCount}]],
                    [[${otherCount}]],
                    [[${preferNotToSayCount}]]
                ],
                backgroundColor: [
                    '#FF6384',
                    '#36A2EB',
                    '#FFCE56',
                    '#4BC0C0',
                    '#9966FF'
                ],
                borderColor: '#ffffff',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            aspectRatio: 1,
            plugins: {
                legend: {
                    position: 'top',
                },
                tooltip: {
                    callbacks: {
                        label: function (tooltipItem) {
                            let label = tooltipItem.label || '';
                            if (label) {
                                label += ': ';
                            }
                            label += tooltipItem.raw;
                            return label;
                        }
                    }
                }
            }
        }
    });
});