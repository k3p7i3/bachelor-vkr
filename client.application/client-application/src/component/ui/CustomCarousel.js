import Carousel from "react-multi-carousel"
import ArrowLeftIcon from '@mui/icons-material/ArrowLeft';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';

const CustomLeftArrow = ({ onClick, ...rest }) => {
  const {
    onMove,
    carouselState: { currentSlide, deviceType }
  } = rest;
  return <ArrowLeftIcon
    color="textPrimary"
    fontSize="large"
    className="carousel-container::before carousel-left-arrow" 
    onClick={() => onClick()} 
  />;
};

const CustomRightArrow = ({ onClick, ...rest }) => {
  const {
    onMove,
    carouselState: { currentSlide, deviceType }
  } = rest;
  return <ArrowRightIcon 
    color="textPrimary"
    fontSize="large"
    className="carousel-container::after carousel-right-arrow" 
    onClick={() => onClick()} 
  />;
};

export default function CustomCarousel({ children, items, slidesToSlide }) {
  const responsive = {
    desktop: {
      breakpoint: {
        max: 3000,
        min: 1024
      },
      items: items.large,
      partialVisibilityGutter: 40
    },
    mobile: {
      breakpoint: {
        max: 464,
        min: 0
      },
      items: items.small,
      partialVisibilityGutter: 30
    },
    tablet: {
      breakpoint: {
        max: 1024,
        min: 464
      },
      items: items.medium,
      partialVisibilityGutter: 30
    }
  };

  return <Carousel
    additionalTransfrom={0}
    arrows
    centerMode={false}
    className=""
    containerClass="carousel-container"
    dotListClass=""
    draggable={false}
    focusOnSelect={false}
    infinite={false}
    itemClass="carousel-item"
    keyBoardControl
    minimumTouchDrag={80}
    pauseOnHover
    customLeftArrow={<CustomLeftArrow/>}
    customRightArrow={<CustomRightArrow/>}
    renderArrowsWhenDisabled={false}
    renderButtonGroupOutside={false}
    renderDotsOutside={false}
    responsive={responsive}
    rewind={false}
    rewindWithAnimation={false}
    rtl={false}
    shouldResetAutoplay
    showDots={false}
    sliderClass=""
    slidesToSlide={slidesToSlide}
    swipeable
  >
    {children}
  </Carousel>;
}