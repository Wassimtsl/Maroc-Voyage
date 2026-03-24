import React from 'react';
import { cn } from '@/lib/utils';

interface GlassCardProps extends React.HTMLAttributes<HTMLDivElement> {
  children: React.ReactNode;
  variant?: 'default' | 'strong';
  hover?: boolean;
}

const GlassCard: React.FC<GlassCardProps> = ({
  children,
  className,
  variant = 'default',
  hover = false,
  ...props
}) => {
  return (
    <div
      className={cn(
        'rounded-2xl p-6 transition-all duration-500',
        variant === 'default' ? 'glass-card' : 'glass-card-strong',
        hover && 'hover-lift cursor-pointer',
        className
      )}
      {...props}
    >
      {children}
    </div>
  );
};

export default GlassCard;
