/*
	Lens by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
*/

var main = (function($) { var _ = {

	/**
	 * Settings.
	 * @var {object}
	 */
	settings: {

		// Preload all images.
			preload: false,

		// Slide duration (must match "duration.slide" in _vars.scss).
			slideDuration: 500,

		// Layout duration (must match "duration.layout" in _vars.scss).
			layoutDuration: 750,

		// Thumbnails per "row" (must match "misc.thumbnails-per-row" in _vars.scss).
			thumbnailsPerRow: 2,

		// Side of main wrapper (must match "misc.main-side" in _vars.scss).
			mainSide: 'right'

	},

	/**
	 * Window.
	 * @var {jQuery}
	 */
	$window: null,

	/**
	 * Body.
	 * @var {jQuery}
	 */
	$body: null,

	/**
	 * Main wrapper.
	 * @var {jQuery}
	 */
	$main: null,

	/**
	 * Thumbnails.
	 * @var {jQuery}
	 */
	$thumbnails: null,

	/**
	 * Viewer.
	 * @var {jQuery}
	 */
	$viewer: null,

	/**
	 * Toggle.
	 * @var {jQuery}
	 */
	$toggle: null,

	/**
	 * Nav (next).
	 * @var {jQuery}
	 */
	$navNext: null,

	/**
	 * Nav (previous).
	 * @var {jQuery}
	 */
	$navPrevious: null,

	/**
	 * Slides.
	 * @var {array}
	 */
	slides: [],

	/**
	 * Current slide index.
	 * @var {integer}
	 */
	current: null,

	/**
	 * Lock state.
	 * @var {bool}
	 */
	locked: false,

	/**
	 * Keyboard shortcuts.
	 * @var {object}
	 */
	keys: {

		// Escape: Toggle main wrapper.
			27: function() {
				_.toggle();
			},

		// Up: Move up.
			38: function() {
				_.up();
			},

		// Down: Move down.
			40: function() {
				_.down();
			},

		// Space: Next.
			32: function() {
				_.next();
			},

		// Right Arrow: Next.
			39: function() {
				_.next();
			},

		// Left Arrow: Previous.
			37: function() {
				_.previous();
			}

	},

	/**
	 * Initialize properties.
	 */
	initProperties: function() {

		// Window, body.
			_.$window = $(window);
			_.$body = $('body');

		// Thumbnails.
			_.$thumbnails = $('#thumbnails');

		// Viewer.
			_.$viewer = $(
				'<div id="viewer">' +
					'<div class="inner">' +
						'<div class="nav-next"></div>' +
						'<div class="nav-previous"></div>' +
						'<div class="toggle"></div>' +
					'</div>' +
				'</div>'
			).appendTo(_.$body);

		// Nav.
			_.$navNext = _.$viewer.find('.nav-next');
			_.$navPrevious = _.$viewer.find('.nav-previous');

		// Main wrapper.
			_.$main = $('#main');

		// Toggle.
			$('<div class="toggle"></div>')
				.appendTo(_.$main);

			_.$toggle = $('.toggle');

	},

	/**
	 * Initialize events.
	 */
	initEvents: function() {

		// Window.

			// Remove is-preload-* classes on load.
				_.$window.on('load', function() {

					_.$body.removeClass('is-preload-0');

					window.setTimeout(function() {
						_.$body.removeClass('is-preload-1');
					}, 100);

					window.setTimeout(function() {
						_.$body.removeClass('is-preload-2');
					}, 100 + Math.max(_.settings.layoutDuration - 150, 0));

				});

			// Disable animations/transitions on resize.
				var resizeTimeout;

				_.$window.on('resize', function() {

					_.$body.addClass('is-preload-0');
					window.clearTimeout(resizeTimeout);

					resizeTimeout = window.setTimeout(function() {
						_.$body.removeClass('is-preload-0');
					}, 100);

				});

		// Viewer.

			// Hide main wrapper on tap (<= medium only).
				_.$viewer.on('touchend', function() {

					if (breakpoints.active('<=medium'))
						_.hide();

				});

			// Touch gestures.
				_.$viewer
					.on('touchstart', function(event) {

						// Record start position.
							_.$viewer.touchPosX = event.originalEvent.touches[0].pageX;
							_.$viewer.touchPosY = event.originalEvent.touches[0].pageY;

					})
					.on('touchmove', function(event) {

						// No start position recorded? Bail.
							if (_.$viewer.touchPosX == null
							||	_.$viewer.touchPosY == null)
								return;

						// Calculate stuff.
							var	diffX = _.$viewer.touchPosX - event.originalEvent.touches[0].pageX,
								diffY = _.$viewer.touchPosY - event.originalEvent.touches[0].pageY;
								boundary = 20,
								delta = 50;

						// Swipe left (next).
							if ( (diffY < boundary && diffY > (-1 * boundary)) && (diffX > delta) )
								_.next();

						// Swipe right (previous).
							else if ( (diffY < boundary && diffY > (-1 * boundary)) && (diffX < (-1 * delta)) )
								_.previous();

						// Overscroll fix.
							var	th = _.$viewer.outerHeight(),
								ts = (_.$viewer.get(0).scrollHeight - _.$viewer.scrollTop());

							if ((_.$viewer.scrollTop() <= 0 && diffY < 0)
							|| (ts > (th - 2) && ts < (th + 2) && diffY > 0)) {

								event.preventDefault();
								event.stopPropagation();

							}

					});

		// Main.

			// Touch gestures.
				_.$main
					.on('touchstart', function(event) {

						// Bail on xsmall.
							if (breakpoints.active('<=xsmall'))
								return;

						// Record start position.
							_.$main.touchPosX = event.originalEvent.touches[0].pageX;
							_.$main.touchPosY = event.originalEvent.touches[0].pageY;

					})
					.on('touchmove', function(event) {

						// Bail on xsmall.
							if (breakpoints.active('<=xsmall'))
								return;

						// No start position recorded? Bail.
							if (_.$main.touchPosX == null
							||	_.$main.touchPosY == null)
								return;

						// Calculate stuff.
							var	diffX = _.$main.touchPosX - event.originalEvent.touches[0].pageX,
								diffY = _.$main.touchPosY - event.originalEvent.touches[0].pageY;
								boundary = 20,
								delta = 50,
								result = false;

						// Swipe to close.
							switch (_.settings.mainSide) {

								case 'left':
									result = (diffY < boundary && diffY > (-1 * boundary)) && (diffX > delta);
									break;

								case 'right':
									result = (diffY < boundary && diffY > (-1 * boundary)) && (diffX < (-1 * delta));
									break;

								default:
									break;

							}

							if (result)
								_.hide();

						// Overscroll fix.
							var	th = _.$main.outerHeight(),
								ts = (_.$main.get(0).scrollHeight - _.$main.scrollTop());

							if ((_.$main.scrollTop() <= 0 && diffY < 0)
							|| (ts > (th - 2) && ts < (th + 2) && diffY > 0)) {

								event.preventDefault();
								event.stopPropagation();

							}

					});
		// Toggle.
			_.$toggle.on('click', function() {
				_.toggle();
			});

			// Prevent event from bubbling up to "hide event on tap" event.
				_.$toggle.on('touchend', function(event) {
					event.stopPropagation();
				});

		// Nav.
			_.$navNext.on('click', function() {
				_.next();
			});

			_.$navPrevious.on('click', function() {
				_.previous();
			});

		// Keyboard shortcuts.

			// Ignore shortcuts within form elements.
				_.$body.on('keydown', 'input,select,textarea', function(event) {
					event.stopPropagation();
				});

			_.$window.on('keydown', function(event) {

				// Ignore if xsmall is active.
					if (breakpoints.active('<=xsmall'))
						return;

				// Check keycode.
					if (event.keyCode in _.keys) {

						// Stop other events.
							event.stopPropagation();
							event.preventDefault();

						// Call shortcut.
							(_.keys[event.keyCode])();

					}

			});

	},

	/**
	 * Initialize viewer.
	 */
	initViewer: function() {

		// Bind thumbnail click event.
			_.$thumbnails
				.on('click', '.thumbnail', function(event) {

					var $this = $(this);

					// Stop other events.
						event.preventDefault();
						event.stopPropagation();

					// Locked? Blur.
						if (_.locked)
							$this.blur();

					// Switch to this thumbnail's slide.
						_.switchTo($this.data('index'));


				});


		// Create slides from thumbnails.
        _.$thumbnails.children()
            .each(function(index) {

                var	$this = $(this),
                    $thumbnail = $this.children('.thumbnail'),
                    s;

                // Slide object.
                s = {
                    $parent: $this,
                    $slide: null,
                    $slideImage: null,
					$slideElement: null,
                    $slideCaption: null,
                    $slideCanvasContainer: null,
					$slideSankeyContainer: null,
                    url: $thumbnail.attr('href'),
                    loaded: false
                };

                // Parent.
                $this.attr('tabIndex', '-1');

                // Slide.

                // Create elements.
                // s.$slide = $('<div class="slide"><canvas id="canvas""></canvas><div class="caption"></div></div>');
                s.$slide = $('<div class="slide">' +
								'<div class="sankeyContainer">' +
								'<div id="sankey_major"></div>' +
								'<div id="sankey_minor"></div>' +
								'<div id="sankey_sports"></div>' +
								'</div>' +
								'<div class="canvasContainer">' +
									'<canvas id="canvas"></canvas>' +
								'</div>' +
								'<div class="caption"></div>' +
							'</div>');


                // Image.
                s.$slideImage = s.$slide.children('.image');
                // s.$slideElement = s.$slide.children(".element");
				s.$slideCanvasContainer = s.$slide.children('.canvasContainer');
				s.$slideSankeyContainer = s.$slide.children('.sankeyContainer');

                //_.$thumbnails.children().eq(0).$slideElement.load("../html/sankey.html");
				//s.$slideElement.load("../html/sankey.html");
				switch (index) {
					case 0:
                        // s.$slideElement.load("../html/main.html");
                        break;

					case 1:
                        // s.$slideElement.load("../html/sankey.html");
                        break;

                    case 2:
                        // s.$slideElement.load("../html/line.html");
                        break;
                }

                // // Set background stuff.
                // s.$slideImage
                //     .css('background-image', '')
                //     .css('background-position', ($thumbnail.data('position') || 'center'));

                // Caption.
                s.$slideCaption = s.$slide.find('.caption');

                // Move everything *except* the thumbnail itself to the caption.
                $this.children().not($thumbnail)
                    .appendTo(s.$slideCaption);

                // Add to slides array.
                _.slides.push(s);

                // Set thumbnail's index.
                $thumbnail.data('index', _.slides.length - 1);

            });

    },

	/**
	 * Initialize stuff.
	 */
	init: function() {

		// Breakpoints.
			breakpoints({
				xlarge:  [ '1281px',  '1680px' ],
				large:   [ '981px',   '1280px' ],
				medium:  [ '737px',   '980px'  ],
				small:   [ '481px',   '736px'  ],
				xsmall:  [ null,      '480px'  ]
			});

		// Everything else.
			_.initProperties();
			_.initViewer();
			_.initEvents();

		// Show first slide if xsmall isn't active.
			breakpoints.on('>xsmall', function() {

				if (_.current == null)
					_.switchTo(0, true);

			});

	},

	/**
	 * Switch to a specific slide.
	 * @param {integer} index Index.
	 */
	switchTo: function(index, noHide) {
        console.log("index : " + index + ", noHide : " + noHide + ", currentSlide : " + _.current);

		// Already at index and xsmall isn't active? Bail.
			if (_.current == index
			&&	!breakpoints.active('<=xsmall'))
				return;

		// Locked? Bail.
			if (_.locked)
				return;

		// Lock.
			_.locked = true;

		// Hide main wrapper if medium is active.
			if (!noHide
			&&	breakpoints.active('<=medium'))
				_.hide();


		// Get slides.
			var	oldSlide = (_.current !== null ? _.slides[_.current] : null),
				newSlide = _.slides[index];

        // Update current.
			_.current = index;

		// Deactivate old slide (if there is one).
			if (oldSlide) {

				// Thumbnail.
					oldSlide.$parent
						.removeClass('active');

				// Slide.
					oldSlide.$slide.removeClass('active');

					// 화면 초기화
					var canvas = oldSlide.$slideCanvasContainer.children()[0];
					var context = canvas.getContext('2d');
					context.clearRect(0, 0, canvas.width, canvas.height);

					// var charts = oldSlide.$slideSankeyContainer.children();
					// for (var i = 0; i <= charts.length; i++)
					// 	charts[i] = "";
			}

		// Activate new slide.

			// Thumbnail.
				newSlide.$parent
					.addClass('active')
					.focus();

			// Slide.
				var f = function() {

					// Old slide exists? Detach it.
						if (oldSlide)
                            oldSlide.$slide.detach();

					// Attach new slide.
						newSlide.$slide.appendTo(_.$viewer);

					// New slide not yet loaded?
						if (!newSlide.loaded) {

							window.setTimeout(function() {

								// Mark as loading.
									newSlide.$slide.addClass('loading');

								// Wait for it to load.
									$('<img src="' + newSlide.url + '" />').on('load', function() {
									//window.setTimeout(function() {

										// Set background image.
											newSlide.$slideImage
												.css('background-image', 'url(' + newSlide.url + ')');

										// Mark as loaded.
											newSlide.loaded = true;
											newSlide.$slide.removeClass('loading');

										// Mark as active.
											newSlide.$slide.addClass('active');

										// Unlock.
											window.setTimeout(function() {
												_.locked = false;
											}, 100);

									//}, 1000);
									});

							}, 100);

						}

					// Otherwise ...
						else {

							window.setTimeout(function() {

								// Mark as active.
									newSlide.$slide.addClass('active');

								// Unlock.
									window.setTimeout(function() {
										_.locked = false;
									}, 100);

							}, 100);

						}

				};
				
				// No old slide? Switch immediately.
					if (!oldSlide)
						(f)();

				// Otherwise, wait for old slide to disappear first.
					else
						window.setTimeout(f, _.settings.slideDuration);

					// 새로운 슬라이드가 로딩되고 수행하려는 함수
					if (index == 0) {	// wordcloud load
                        // var canvasContainer = newSlide.$slide.children(".canvasContainer")[0];
						// var canvas = canvasContainer.children();
                        // var canvasContainer = newSlide.$slideCanvasContainer.children()[0];
						// console.log(canvasContainer);
						// console.log(canvas);
                        // _.wordcloud_load(newSlide.$slide.children("#canvas")[0]);

						// canvas를 매개변수로 넘겨줌
						_.wordcloud_load(newSlide.$slideCanvasContainer.children()[0]);
					} else if (index == 1) {
                        initSankey();
                        // $(document).ready(initSankey());
					}
	},

	/**
	 * Switches to the next slide.
	 */
	next: function() {

		// Calculate new index.
			var i, c = _.current, l = _.slides.length;

			if (c >= l - 1)
				i = 0;
			else
				i = c + 1;

		// Switch.
			_.switchTo(i);

	},

	/**
	 * Switches to the previous slide.
	 */
	previous: function() {

		// Calculate new index.
			var i, c = _.current, l = _.slides.length;

			if (c <= 0)
				i = l - 1;
			else
				i = c - 1;

		// Switch.
			_.switchTo(i);

	},

	/**
	 * Switches to slide "above" current.
	 */
	up: function() {

		// Fullscreen? Bail.
			if (_.$body.hasClass('fullscreen'))
				return;

		// Calculate new index.
			var i, c = _.current, l = _.slides.length, tpr = _.settings.thumbnailsPerRow;

			if (c <= (tpr - 1))
				i = l - (tpr - 1 - c) - 1;
			else
				i = c - tpr;

		// Switch.
			_.switchTo(i);

	},

	/**
	 * Switches to slide "below" current.
	 */
	down: function() {

		// Fullscreen? Bail.
			if (_.$body.hasClass('fullscreen'))
				return;

		// Calculate new index.
			var i, c = _.current, l = _.slides.length, tpr = _.settings.thumbnailsPerRow;

			if (c >= l - tpr)
				i = c - l + tpr;
			else
				i = c + tpr;

		// Switch.
			_.switchTo(i);

	},

	/**
	 * Shows the main wrapper.
	 */
	show: function() {

		// Already visible? Bail.
			if (!_.$body.hasClass('fullscreen'))
				return;

		// Show main wrapper.
			_.$body.removeClass('fullscreen');

		// Focus.
			_.$main.focus();

	},

	/**
	 * Hides the main wrapper.
	 */
	hide: function() {

		// Already hidden? Bail.
			if (_.$body.hasClass('fullscreen'))
				return;

		// Hide main wrapper.
			_.$body.addClass('fullscreen');

		// Blur.
			_.$main.blur();

	},

	/**
	 * Toggles main wrapper.
	 */
	toggle: function() {

		if (_.$body.hasClass('fullscreen'))
			_.show();
		else
			_.hide();

	},

	wordcloud_load: function (canvas) {

        var data = null;
        var arr = [];
        var parent = document.getElementById("viewer");

        $.ajax({
            url: "/WordCloud",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(data),
            success: function(result) {

                // arr = [];
                arr.length = 0;

                for (var i = 0; i < result.length; i++) {
                    arr.push( [ result[i].word,
                        result[i].totalWeight,
                        result[i].idList ] );
                }

                var options = {
                    list : arr,
                    // gridSize: Math.round(2 * parent.offsetHeight / 1024),
                    // weightFactor: function (size) {
                    //     return Math.pow(size, 2) * parent.offsetWidth / 1024;
                    // },
                    weightFactor: 5,
                    minSize: 3,
                    figPath: "circle",
                    // backgroundColor: "white",
                    click: function(item) {
                        alert("word : " + item[0] + " totalWeight : " + item[1]);
                        location.href= "/mainNewsList?"+item[2];

                        //popup 창
						// window.open("/mainNewsList?"+item[2], "newsList", 'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes')
                    }
                }


                canvas.width = parent.offsetWidth;
                canvas.height = parent.offsetHeight;

                WordCloud(canvas, options);
            },
            error : function () {
                alert("fail");
            }
        })

    },

	//
	// initSankey: function() {
	// 	google.charts.load('current', {'packages':['sankey']});
	//
    //     google.charts.setOnLoadCallback(_.sankey_major_drawChart());
    //     google.charts.setOnLoadCallback(_.sankey_minor_drawChart());
    //     google.charts.setOnLoadCallback(_.sankey_sports_drawChart());
    // },
	//
    // sankey_major_drawChart: function() {
    //     var inputdata = [];
	//
    //     $.ajax({
    //         url: "/sankey_major_post",
    //         type: "POST",
    //         dataType: "json",
    //         contentType: "application/json",
    //         data: JSON.stringify(null),
    //         success: function(data) {
	//
    //             for (var i = 0; i < data.length; i++)
    //                 inputdata.push( [ data[i].source, data[i].destination, data[i].value ] );
	//
    //             try {
    //                 var chartData = new google.visualization.DataTable();
	// 			} catch (e) {
	// 				console.log(e.message);
    //             }
    //             // var chartData = new google.visualization.DataTable();
    //             chartData.addColumn('string', 'From');
    //             chartData.addColumn('string', 'To');
    //             chartData.addColumn('number', 'Weight');
    //             chartData.addRows(inputdata);
	//
    //             var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
    //                 '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];
	//
    //             // Sets chart options.
    //             var options = {
    //                 width: 400,
    //                 height: 400,
    //                 sankey: {
    //                     node: {
    //                         colors: colors
    //                     },
    //                     link: {
    //                         colorMode: 'gradient',
    //                         colors: colors
    //                     }
    //                 }
    //             };
	//
    //             // Instantiates and draws our chart, passing in some options.
    //             var chart = new google.visualization.Sankey(document.getElementById('sankey_major'));
    //             chart.draw(data, options);
    //         }
    //     })
	// },
	//
    // sankey_minor_drawChart: function() {
    //     var inputdata = [];
	//
    //     $.ajax({
    //         url: "/sankey_minor_post",
    //         type: "POST",
    //         dataType: "json",
    //         contentType: "application/json",
    //         data: JSON.stringify(null),
    //         success: function(data) {
	//
    //             for (var i = 0; i < data.length; i++)
    //                 inputdata.push( [ data[i].source, data[i].destination, data[i].value ] );
	//
    //             try {
    //                 var chartData = new google.visualization.DataTable();
    //             } catch (e) {
    //                 console.log(e.message);
    //             }
    //             // var chartData = new google.visualization.DataTable();
    //             chartData.addColumn('string', 'From');
    //             chartData.addColumn('string', 'To');
    //             chartData.addColumn('number', 'Weight');
    //             chartData.addRows(inputdata);
	//
    //             var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
    //                 '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];
	//
    //             // Sets chart options.
    //             var options = {
    //                 width: 400,
    //                 height: 400,
    //                 sankey: {
    //                     node: {
    //                         colors: colors
    //                     },
    //                     link: {
    //                         colorMode: 'gradient',
    //                         colors: colors
    //                     }
    //                 }
    //             };
	//
    //             // Instantiates and draws our chart, passing in some options.
    //             var chart = new google.visualization.Sankey(document.getElementById('sankey_minor'));
    //             chart.draw(data, options);
    //         }
    //     })
	// },
	//
    // sankey_sports_drawChart: function() {
    //     var inputdata = [];
	//
    //     $.ajax({
    //         url: "/sankey_sports_post",
    //         type: "POST",
    //         dataType: "json",
    //         contentType: "application/json",
    //         data: JSON.stringify(null),
    //         success: function(data) {
    //             for (var i = 0; i < data.length; i++)
    //                 inputdata.push( [ data[i].source, data[i].destination, data[i].value ] );
	//
    //             try {
    //                 var chartData = new google.visualization.DataTable();
    //             } catch (e) {
    //                 console.log(e.message);
    //             }
    //             // var chartData = new google.visualization.DataTable();
    //             chartData.addColumn('string', 'From');
    //             chartData.addColumn('string', 'To');
    //             chartData.addColumn('number', 'Weight');
    //             chartData.addRows(inputdata);
	//
    //             var colors = ['#a6cee3', '#b2df8a', '#fb9a99', '#fdbf6f',
    //                 '#cab2d6', '#ffff99', '#1f78b4', '#33a02c'];
	//
    //             // Sets chart options.
    //             var options = {
    //                 width: 400,
    //                 height: 400,
    //                 sankey: {
    //                     node: {
    //                         colors: colors
    //                     },
    //                     link: {
    //                         colorMode: 'gradient',
    //                         colors: colors
    //                     }
    //                 }
    //             };
	//
    //             // Instantiates and draws our chart, passing in some options.
    //             var chart = new google.visualization.Sankey(document.getElementById('sankey_sports'));
    //             chart.draw(data, options);
    //         }
    //     })
    // },


}; return _; })(jQuery); main.init();

