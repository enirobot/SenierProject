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
                    $slideCaption: null,
                    $slideCanvasContainer: null,
					$slideSankeyContainer: null,
					$slideLineContainer: null,
					$slideBubbleContainer: null,
					$slidepieContainer:null,
                    url: $thumbnail.attr('href'),
                    loaded: false
                };

                // Parent.
                $this.attr('tabIndex', '-1');

                // Slide.
				if (index == 0) {
                    // Create elements.
                    // s.$slide = $('<div class="slide"><canvas id="canvas""></canvas><div class="caption"></div></div>');

                    s.$slide = $('<div class="slide">' +
                        '<div class="canvasContainer">' +
                        	'<canvas id="canvas"></canvas>' +
                        '</div>' +
                        '<div class="caption"></div>' +

                        '<div class="searchBar">' +
							'<span>' +
								'<input class="input" id="searchWordCloud" type="text" placeholder="검색어 입력">' +
								'<button class="button" type="submit">검색</button>' +
							'</span>' +
                        '</div>' +

                        '<div id="myModal" class="modal">' +
                        '<div class="modal-content">' +
                        '<span class="close">&times;</span>' +		// 모달 닫기
                        '<div id="modal_list">' +
                        '</div>' +
                        '</div>' +
                        '</div>' +
                        '</div>');

                    s.$slideCanvasContainer = s.$slide.children('.canvasContainer');
				} else if (index == 1) {
                    s.$slide = $('<div class="slide">' +
									'<div class="sankeyContainer">' +
										'<div id="sankey"></div>' +
									'</div>' +
									'<div class="caption"></div>' +
								'</div>');

                    s.$slideSankeyContainer = s.$slide.children('.sankeyContainer');
				} else  if(index == 2){
                    s.$slide = $('<div class="slide">' +
                        			'<div class="lineContainer">' +
                        				'<div id="linechart"></div>' +
                        			'</div>' +
                        			'<div class="caption"></div>' +
                        		'</div>');

                    s.$slideLineContainer = s.$slide.children('.lineContainer');
				} else if(index == 3){
                    s.$slide = $('<div class="slide">' +
                        '<div class="pieContainer">' +
                        '<div id="piechart"></div>' +
                        '</div>' +
                        '<div class="caption"></div>' +
                        '</div>');

                    s.$slidePieContainer = s.$slide.children('.pieContainer');
                } else if(index == 4){
                    s.$slide = $('<div class="slide">' +
                        '<div class="bubbleContainer">' +
                        '<div id="bubblechart"></div>' +
                        '</div>' +
                        '<div class="caption"></div>' +
                        '</div>');

                    s.$slideBubbleContainer = s.$slide.children('.bubbleContainer');
				} else if(index == 5){
					s.$slide = $(
						'<div id="gameContainer"></div>'
					);

                    $('#gameContainer').load('/templates/game.html');
                } else {
                    s.$slide = $(
                    );
				}

				// image
                s.$slideImage = s.$slide.children('.image');

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
		var oldIndex = 0;
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

        oldIndex = _.current;
        _.hide();

        // Update current.
			_.current = index;

		// Deactivate old slide (if there is one).
			if (oldSlide) {

				// Thumbnail.
					oldSlide.$parent
						.removeClass('active');

				// Slide.
					oldSlide.$slide.removeClass('active');

					// 전환하기 전 화면이 wordCloud일 때
					if (oldIndex == 0) {
                        // 화면 초기화
                        var canvas = oldSlide.$slideCanvasContainer.children()[0];
                        var context = canvas.getContext('2d');
                        context.clearRect(0, 0, canvas.width, canvas.height);

                        // 모달 내용 초기화 후 비활성화
						modalClose();
                    }
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
						initSankey(newSlide.$slideSankeyContainer.children()[0]);
					} else if (index == 2) {
						initLineChart(newSlide.$slideLineContainer.children()[0]);
					} else if(index == 3) {
						initPie(newSlide.$slidePieContainer.children()[0]);
					} else if (index == 4) {
                        initBubble(newSlide.$slideBubbleContainer.children()[0]);
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
                    weightFactor: 7,
                    minSize: 3,
                    figPath: "circle",
                    // backgroundColor: "white",
                    click: function(item) {
                        // alert("word : " + item[0] + " totalWeight : " + item[1]);

                        // location.href= "/mainNewsList?"+item[2];
                        _.sampleModalPopup(item[2], "/newsList");

                        //popup 창
						// window.open("/mainNewsList?"+item[2], "newsList", 'height=' + screen.height + ',width=' + screen.width + 'fullscreen=yes')
                    }
                }

                canvas.width = window.innerWidth;
                canvas.height = window.innerHeight;

                WordCloud(canvas, options);
            },
            error : function () {
                alert("fail");
            }
        })

    },

    sampleModalPopup: function(idList, server_url){
		// console.log(idList);

		$.ajax({
			url: server_url,
			type: "POST",
			dataType: "json",
			contentType: "application/json",
			data: JSON.stringify(idList),
			success: function(result) {
				// console.log(result);

				for (var i = 0; i < result.length; i++) {
					console.log(window.innerWidth);

                    $("#modal_list").append(
                        // "<div class='newsListRow'>" +
                        // 	"<div class = 'newsList_div1'>" +
                        // 	"	<a href=" + result[i].url + ">" + result[i].title + "</a>" +
                        // 	"</div>" +
						// 	"<div class='newsList_div2'>" +
						// 		"<div class='left_div'>" + result[i].company + "</div>" +
						// 		"<div class='right_div'>" + result[i].date + "</div>" +
						// 	"</div>" +
                        // "</div>"
						"<div class='newsListRow'>" +
						"<table class='newsListTable'>" +
							"<tbody>" +
								"<tr class='newsList_tr1'>" +
									"<td class='newsList_cell' colspan='2'>" +
										"<a href=" + result[i].url + ">" + result[i].title + "</a>" +
									"</td>" +
								"</tr>" +
								"<tr class='newsList_tr2'>" +
									"<td class='newsList_cell_left'>" + result[i].company + "</td>" +
									"<td class='newsList_cell_right'>" + result[i].date + "</td>" +
								"</tr>" +
							"</tbody>" +
						"</table>" +
						"</div>"

                        // "<div class='newsListRow'>" +
						// 	"<div class = 'td1'>" +
						// 	"	<a href=" + result[i].url + ">" + result[i].title + "</a>" +
						// 	"</div>" +
						// 	"<div class='td2'>" + result[i].company + "</div>" +
						// 	"<div class='td3'>" + result[i].date + "</div>" +
                        // "</div>"
                    );
                }

				modalOpen();

				},
			error : function () {
				alert("fail");
			}
		});
	},

    findWordCloud: function () {

        var arr = [];

        $.ajax({
            url: "/findKeyword",
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify( $("#searchWordCloud").val() ),
            success: function(result) {

                arr.length = 0;

                for (var i = 0; i < result.length; i++) {
                    arr.push( [ result[i].word,
                        result[i].counts,
                        result[i].idList ] );
                }

                var options = {
                    list : arr,
                    // gridSize: Math.round(2 * document.getElementById('canvas').offsetWidth / 1024),
                    // weightFactor: function (size) {
                    //     return Math.pow(size, 2) * document.getElementById('canvas').offsetWidth / 1024;
                    // },
                    weightFactor: 7,
                    minSize: 3,
                    figPath: "circle",
                    click: function(item) {
                    	console.log(item[0]);
                    	console.log(item[1]);
                    	console.log(item[2]);

                        _.sampleModalPopup(item[2], "/searchNewsList");
                    }
                }

                WordCloud(document.getElementById('canvas'), options);
            },
            error : function () {
                alert("fail");
            }
        })
    },

}; return _; })(jQuery); main.init();

