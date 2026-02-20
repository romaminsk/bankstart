/**
 * BankStart MVP - Main JavaScript
 * 
 * ОБУЧЕНИЕ: ПОЧЕМУ МИНИМУМ JS?
 * ───────────────────────────────────────────────────────
 * 
 * Для MVP используем Server-Side Rendering (Thymeleaf):
 * - Проще архитектура
 * - SEO из коробки
 * - Быстрая разработка
 * 
 * JS используется только для:
 * - Валидации форм (UX)
 * - Графиков Chart.js
 * - Мелких UI улучшений
 */

'use strict';

/**
 * Инициализация при загрузке страницы
 */
document.addEventListener('DOMContentLoaded', function() {
    initFormValidation();
    initTooltips();
    console.log('BankStart MVP initialized');
});

/**
 * Bootstrap Form Validation
 * 
 * ОБУЧЕНИЕ: CLIENT-SIDE vs SERVER-SIDE VALIDATION
 * ───────────────────────────────────────────────────────
 * 
 * Client-side (JS):
 * + Быстрый отклик (UX)
 * + Не грузит сервер
 * - Можно обойти!
 * 
 * Server-side (Spring Validation):
 * + Надёжно (MANDATORY)
 * + Нельзя обойти
 * 
 * ПРАВИЛО: ВСЕГДА валидируйте НА СЕРВЕРЕ!
 * Client-side - только для UX.
 */
function initFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    
    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Инициализация Bootstrap Tooltips
 */
function initTooltips() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    tooltipTriggerList.forEach(function(tooltipTriggerEl) {
        new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Форматирование числа как валюты (BYN)
 * @param {number} value - число
 * @returns {string} форматированная строка
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('ru-BY', {
        style: 'currency',
        currency: 'BYN',
        minimumFractionDigits: 0,
        maximumFractionDigits: 0
    }).format(value);
}

/**
 * Создание графика Cash Flow (будет использоваться на странице результатов)
 * @param {string} canvasId - ID canvas элемента
 * @param {object} data - данные для графика
 */
function createCashFlowChart(canvasId, data) {
    const ctx = document.getElementById(canvasId);
    if (!ctx) return;
    
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: ['M1', 'M2', 'M3', 'M4', 'M5', 'M6', 'M7', 'M8', 'M9', 'M10', 'M11', 'M12'],
            datasets: [
                {
                    label: 'Оптимистичный (P90)',
                    data: data.p90,
                    borderColor: 'rgba(40, 167, 69, 1)',
                    backgroundColor: 'rgba(40, 167, 69, 0.1)',
                    fill: '+1',
                    tension: 0.4
                },
                {
                    label: 'Средний (P50)',
                    data: data.p50,
                    borderColor: 'rgba(13, 110, 253, 1)',
                    backgroundColor: 'rgba(13, 110, 253, 0.1)',
                    fill: '+1',
                    tension: 0.4
                },
                {
                    label: 'Пессимистичный (P10)',
                    data: data.p10,
                    borderColor: 'rgba(220, 53, 69, 1)',
                    backgroundColor: 'rgba(220, 53, 69, 0.1)',
                    fill: 'origin',
                    tension: 0.4
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return context.dataset.label + ': ' + formatCurrency(context.raw);
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: false,
                    ticks: {
                        callback: function(value) {
                            return formatCurrency(value);
                        }
                    }
                }
            }
        }
    });
}
