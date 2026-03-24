import React from 'react';
import { cn } from '@/lib/utils';

interface SkeletonCardProps {
  className?: string;
  variant?: 'event' | 'guide' | 'default';
}

const SkeletonCard: React.FC<SkeletonCardProps> = ({ className, variant = 'default' }) => {
  if (variant === 'event') {
    return (
      <div className={cn('glass-card rounded-2xl overflow-hidden', className)}>
        <div className="h-48 bg-muted animate-pulse" />
        <div className="p-6 space-y-4">
          <div className="h-4 w-24 bg-muted rounded animate-pulse" />
          <div className="h-6 w-3/4 bg-muted rounded animate-pulse" />
          <div className="h-4 w-full bg-muted rounded animate-pulse" />
          <div className="h-4 w-2/3 bg-muted rounded animate-pulse" />
          <div className="flex justify-between items-center pt-4">
            <div className="h-6 w-20 bg-muted rounded animate-pulse" />
            <div className="h-10 w-24 bg-muted rounded-full animate-pulse" />
          </div>
        </div>
      </div>
    );
  }

  if (variant === 'guide') {
    return (
      <div className={cn('glass-card rounded-2xl overflow-hidden', className)}>
        <div className="p-6 flex flex-col items-center text-center space-y-4">
          <div className="w-24 h-24 rounded-full bg-muted animate-pulse" />
          <div className="h-6 w-32 bg-muted rounded animate-pulse" />
          <div className="h-4 w-24 bg-muted rounded animate-pulse" />
          <div className="flex gap-2">
            <div className="h-6 w-16 bg-muted rounded-full animate-pulse" />
            <div className="h-6 w-16 bg-muted rounded-full animate-pulse" />
          </div>
          <div className="h-10 w-full bg-muted rounded-full animate-pulse" />
        </div>
      </div>
    );
  }

  return (
    <div className={cn('glass-card rounded-2xl p-6 space-y-4', className)}>
      <div className="h-4 w-3/4 bg-muted rounded animate-pulse" />
      <div className="h-4 w-full bg-muted rounded animate-pulse" />
      <div className="h-4 w-2/3 bg-muted rounded animate-pulse" />
    </div>
  );
};

export default SkeletonCard;
