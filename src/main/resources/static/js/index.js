// index.js - Animaciones de Scroll Reveal + Parallax

(function() {
    'use strict';

    // ===== REVEAL ON SCROLL =====
    function revealOnScroll() {
        const reveals = document.querySelectorAll('.reveal');
        
        reveals.forEach(element => {
            const windowHeight = window.innerHeight;
            const elementTop = element.getBoundingClientRect().top;
            const elementVisible = 100; // Píxeles antes de que aparezca
            
            if (elementTop < windowHeight - elementVisible) {
                element.classList.add('active');
            }
        });
    }

    // ===== PARALLAX SCROLL EFFECT =====
    function updateParallax() {
        const scrolled = window.pageYOffset;
        const parallaxElements = document.querySelectorAll('.parallax-bg');
        
        parallaxElements.forEach((el) => {
            const section = el.closest('section');
            if (!section) return;
            
            const sectionTop = section.offsetTop;
            const sectionHeight = section.offsetHeight;
            const scrollRelative = scrolled - sectionTop + window.innerHeight;
            
            // Solo aplicar parallax si la sección está visible
            if (scrollRelative > 0 && scrolled < sectionTop + sectionHeight) {
                const speed = 0.5; // Velocidad del parallax
                const yPos = -(scrollRelative * speed);
                el.style.transform = `translate3d(0, ${yPos}px, 0)`;
            }
        });
    }

    // ===== NAVBAR SCROLL EFFECT =====
    function updateNavbar() {
        const navbar = document.querySelector('.navbar');
        if (!navbar) return;
        
        const scrolled = window.pageYOffset;
        
        if (scrolled > 100) {
            navbar.classList.add('scrolled');
        } else {
            navbar.classList.remove('scrolled');
        }
    }

    // ===== OPTIMIZED SCROLL HANDLER =====
    let ticking = false;
    function onScroll() {
        if (!ticking) {
            window.requestAnimationFrame(() => {
                revealOnScroll();
                updateParallax();
                updateNavbar();
                ticking = false;
            });
            ticking = true;
        }
    }

    // ===== SMOOTH SCROLL PARA ANCLAS =====
    function initSmoothScroll() {
        document.querySelectorAll('a[href^="#"]').forEach(anchor => {
            anchor.addEventListener('click', function(e) {
                const href = this.getAttribute('href');
                if (href === '#' || !href) return;
                
                e.preventDefault();
                const target = document.querySelector(href);
                
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    }

    // ===== CARD HOVER EFFECT =====
    function initCardEffects() {
        const cards = document.querySelectorAll('.event-card');
        
        cards.forEach(card => {
            card.addEventListener('mouseenter', function() {
                this.style.transition = 'transform 0.4s ease';
            });
            
            card.addEventListener('mouseleave', function() {
                this.style.transform = '';
            });
        });
    }

    // ===== INICIALIZACIÓN =====
    function init() {
        // Ejecutar reveal inicial
        revealOnScroll();
        updateParallax();
        updateNavbar();
        
        // Inicializar smooth scroll
        initSmoothScroll();
        
        // Inicializar efectos de tarjetas
        initCardEffects();
        
        console.log('✨ EventLink initialized');
    }

    // ===== EVENT LISTENERS =====
    window.addEventListener('scroll', onScroll);
    
    // Ejecutar cuando el DOM esté listo
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }

    // También ejecutar después de que Thymeleaf cargue contenido
    window.addEventListener('load', () => {
        setTimeout(revealOnScroll, 100);
    });

})();